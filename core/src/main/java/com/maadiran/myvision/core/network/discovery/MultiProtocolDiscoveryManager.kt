package com.maadiran.myvision.core.network.discovery

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.net.wifi.WifiManager
import android.util.Log
import com.maadiran.myvision.core.model.DeviceInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.isActive
import java.net.*
import java.util.concurrent.ConcurrentHashMap
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener
import java.io.Closeable

class MultiProtocolDiscoveryManager(
    private val context: Context,
    private val deviceUpdateListener: DeviceUpdateListener,
    private val coroutineScope: CoroutineScope
) : Closeable {
    private val TAG = "TVDiscovery"
    private val updateThrottleTime = 500L // ms
    private var lastUpdateTime = 0L
    private var jmDNS: JmDNS? = null
    private var nsdManager: NsdManager? = null
    private val discoveredDevices = mutableSetOf<DeviceInfo>()
    private var multicastLock: WifiManager.MulticastLock? = null

    private val activeDiscoveryListeners = ConcurrentHashMap<String, NsdManager.DiscoveryListener>()
    private val discoveryJobs = mutableListOf<Job>()
    private var isDiscoveryActive = false

    private val SERVICE_TYPES = arrayOf(
        "_googlecast._tcp.local.",
        "_androidtvremote._tcp.local.",
        "_googletv._tcp.local.",
        "_smarttv._tcp.local."
    )

    init {
        nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
        acquireMulticastLock()
    }

    private fun acquireMulticastLock() {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        multicastLock = wifiManager.createMulticastLock("discoveryMulticastLock").apply {
            setReferenceCounted(true)
        }
    }

    // Add timeouts for discovery
    suspend fun startDiscovery() = withContext(Dispatchers.IO) {
        if (isDiscoveryActive) {
            Log.d(TAG, "Discovery already active, stopping previous session")
            stopDiscovery()
        }

        isDiscoveryActive = true
        discoveredDevices.clear()

        try {
            withTimeout(30000) { // 30 second timeout
                supervisorScope {
                    launch { startMDNSDiscovery() }
                    launch { startNSDDiscovery() }
                    launch { startCastProtocolDiscovery() }
                    launch { performNetworkScan() }
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.d(TAG, "Discovery timeout reached")
        } catch (e: Exception) {
            Log.e(TAG, "Error during discovery", e)
        } finally {
            stopDiscovery()
        }
    }

    private suspend fun startMDNSDiscovery() = withContext(Dispatchers.IO) {
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val multicastLock = wifiManager.createMulticastLock("mdnsDiscovery")
            multicastLock.acquire()

            val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val dhcp = wifi.dhcpInfo
            val intAddress = dhcp.ipAddress
            val address = InetAddress.getByAddress(ByteArray(4).apply {
                set(0, (intAddress and 0xFF).toByte())
                set(1, (intAddress shr 8 and 0xFF).toByte())
                set(2, (intAddress shr 16 and 0xFF).toByte())
                set(3, (intAddress shr 24 and 0xFF).toByte())
            })

            jmDNS = JmDNS.create(address)

            val serviceListener = createServiceListener()

            SERVICE_TYPES.forEach { serviceType ->
                jmDNS?.addServiceListener(serviceType, serviceListener)
            }

            delay(5000) // Search for 5 seconds
            multicastLock.release()
        } catch (e: Exception) {
            Log.e(TAG, "mDNS discovery error", e)
        }
    }

    private fun createServiceListener() = object : ServiceListener {
        override fun serviceAdded(event: ServiceEvent) {
            Log.d(TAG, "Service added: ${event.info}")
            jmDNS?.requestServiceInfo(event.type, event.name)
        }

        override fun serviceRemoved(event: ServiceEvent) {
            Log.d(TAG, "Service removed: ${event.info}")
        }

        override fun serviceResolved(event: ServiceEvent) {
            val device = DeviceInfo(
                name = event.info.name,
                host = event.info.hostAddresses.firstOrNull() ?: return,
                port = event.info.port,
                discoveryMethod = DeviceInfo.DiscoveryMethod.MDNS,
                serviceId = event.info.type
            )
            addDevice(device)
        }
    }

    private suspend fun startNSDDiscovery() = withContext(Dispatchers.Main) {
        SERVICE_TYPES.forEach { serviceType ->
            try {
                // Clean up existing listener
                activeDiscoveryListeners[serviceType]?.let { oldListener ->
                    try {
                        nsdManager?.stopServiceDiscovery(oldListener)
                        activeDiscoveryListeners.remove(serviceType)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error stopping previous discovery", e)
                    }
                }

                delay(500) // Add delay between service types

                val discoveryListener = object : NsdManager.DiscoveryListener {
                    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                        Log.e(TAG, "Failed to start discovery: $errorCode")
                    }

                    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                        Log.e(TAG, "Failed to stop discovery: $errorCode")
                    }

                    override fun onDiscoveryStarted(serviceType: String) {
                        Log.d(TAG, "Discovery started")
                    }

                    override fun onDiscoveryStopped(serviceType: String) {
                        Log.d(TAG, "Discovery stopped")
                    }

                    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                        coroutineScope.launch(Dispatchers.Main) {
                            nsdManager?.resolveService(serviceInfo, createResolveListener())
                        }
                    }

                    override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                        Log.d(TAG, "Service lost: ${serviceInfo.serviceName}")
                    }
                }

                nsdManager?.discoverServices(
                    serviceType,
                    NsdManager.PROTOCOL_DNS_SD,
                    discoveryListener
                )

                activeDiscoveryListeners[serviceType] = discoveryListener
            } catch (e: Exception) {
                Log.e(TAG, "Error in NSD discovery", e)
            }
        }
    }

    private fun createResolveListener() = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.e(TAG, "Failed to resolve service: $errorCode")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            val device = DeviceInfo(
                name = serviceInfo.serviceName,
                host = serviceInfo.host.hostAddress,
                port = serviceInfo.port,
                discoveryMethod = DeviceInfo.DiscoveryMethod.NSD,
                serviceId = serviceInfo.serviceType
            )
            addDevice(device)
        }
    }

    private fun addDevice(device: DeviceInfo) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime < updateThrottleTime) {
            return
        }

        if (discoveredDevices.add(device)) {
            lastUpdateTime = currentTime
            coroutineScope.launch(Dispatchers.Main) {
                deviceUpdateListener.onDevicesUpdated(discoveredDevices.toList())
            }
        }
    }

    private suspend fun startCastProtocolDiscovery() = withContext(Dispatchers.IO) {
        try {
            val socket = DatagramSocket()
            socket.broadcast = true
            socket.soTimeout = 3000

            val message = """
                M-SEARCH * HTTP/1.1
                HOST: 239.255.255.250:1900
                MAN: "ssdp:discover"
                MX: 1
                ST: urn:dial-multiscreen-org:service:dial:1
                USER-AGENT: Android TV Remote/1.0
            """.trimIndent()

            val sendData = message.toByteArray()
            val sendPacket = DatagramPacket(
                sendData,
                sendData.size,
                InetAddress.getByName("239.255.255.250"),
                1900
            )

            socket.send(sendPacket)

            // Receive responses
            val receiveData = ByteArray(1024)
            val receivePacket = DatagramPacket(receiveData, receiveData.size)

            try {
                while (isActive) {
                    socket.receive(receivePacket)
                    val response = String(receivePacket.data, 0, receivePacket.length)
                    processCastResponse(response, receivePacket.address)
                }
            } catch (e: SocketTimeoutException) {
                // Expected timeout
            } finally {
                socket.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Cast protocol discovery error", e)
        }
    }

    private fun processCastResponse(response: String, address: InetAddress) {
        if (response.contains("Google Cast") || response.contains("Android TV")) {
            val device = DeviceInfo(
                name = extractDeviceName(response) ?: "Android TV Device",
                host = address.hostAddress,
                port = extractPort(response) ?: 8008,
                discoveryMethod = DeviceInfo.DiscoveryMethod.CAST
            )
            addDevice(device)
        }
    }

    private suspend fun performNetworkScan() = withContext(Dispatchers.IO) {
        try {
            val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val dhcp = wifi.dhcpInfo
            val baseAddress = dhcp.gateway

            val ports = listOf(8008, 8009, 8443, 9000)

            for (i in 0..254) {
                if (!isActive) break

                val ipAddress = formatIpAddress(baseAddress, i)
                scanPorts(ipAddress, ports)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network scan error", e)
        }
    }

    private suspend fun scanPorts(host: String, ports: List<Int>) {
        ports.forEach { port ->
            if (!isActive) return

            try {
                withTimeout(100) {
                    val socket = Socket()
                    try {
                        socket.connect(InetSocketAddress(host, port), 100)
                        socket.close()
                        testGoogleTVEndpoints(host, port)
                    } catch (e: Exception) {
                        socket.close()
                    }
                }
            } catch (e: Exception) {
                // Timeout or connection error - expected for closed ports
            }
        }
    }

    private fun formatIpAddress(baseAddress: Int, lastOctet: Int): String {
        return String.format(
            "%d.%d.%d.%d",
            baseAddress and 0xFF,
            baseAddress shr 8 and 0xFF,
            baseAddress shr 16 and 0xFF,
            lastOctet
        )
    }

    private suspend fun testGoogleTVEndpoints(host: String, port: Int) = withContext(Dispatchers.IO) {
        try {
            val urls = listOf(
                "http://$host:$port/apps",
                "http://$host:$port/system/info",
                "http://$host:$port/dial/apps"
            )

            urls.forEach { url ->
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connectTimeout = 1000
                    connection.readTimeout = 1000

                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        val device = DeviceInfo(
                            name = "Android TV at $host",
                            host = host,
                            port = port,
                            discoveryMethod = DeviceInfo.DiscoveryMethod.NETWORK_SCAN
                        )
                        addDevice(device)
                    }
                } catch (e: Exception) {
                    // Skip failed endpoint
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error testing Google TV endpoints", e)
        }
    }

    private fun extractDeviceName(response: String): String? {
        return "friendlyName\\.txt=\"([^\"]+)\"".toRegex()
            .find(response)?.groupValues?.get(1)
    }

    private fun extractPort(response: String): Int? {
        return ":([0-9]+)/".toRegex()
            .find(response)?.groupValues?.get(1)?.toIntOrNull()
    }

    suspend fun stopDiscovery() = withContext(Dispatchers.Main) {
        isDiscoveryActive = false

        // Cancel all discovery jobs
        discoveryJobs.forEach { it.cancel() }
        discoveryJobs.clear()

        // Stop NSD discovery
        activeDiscoveryListeners.forEach { (serviceType, listener) ->
            try {
                nsdManager?.stopServiceDiscovery(listener)
            } catch (e: Exception) {
                Log.e(TAG, "Error stopping discovery for $serviceType", e)
            }
        }
        activeDiscoveryListeners.clear()

        // Close JmDNS
        try {
            jmDNS?.close()
            jmDNS = null
        } catch (e: Exception) {
            Log.e(TAG, "Error closing JmDNS", e)
        }

        // Release multicast lock
        try {
            multicastLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing multicast lock", e)
        }

        // Clear discovered devices
        discoveredDevices.clear()
        deviceUpdateListener.onDevicesUpdated(emptyList())
    }

    override fun close() {
        coroutineScope.launch {
            stopDiscovery()
        }
    }
}