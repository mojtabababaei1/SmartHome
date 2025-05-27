package com.maadiran.myvision.core.network.discovery

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.net.wifi.WifiManager
import android.util.Log
import com.maadiran.myvision.core.model.DeviceInfo  // Updated import

class DeviceDiscoveryManager(
    private val context: Context,
    private val serviceType: String // Accept serviceType as a parameter
) {
    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private val discoveredDevices = mutableListOf<DeviceInfo>()
    private var multicastLock: WifiManager.MulticastLock? = null

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        override fun onDiscoveryStarted(regType: String) {
            Log.d("DeviceDiscovery", "Service discovery started for $regType")
        }

        override fun onServiceFound(serviceInfo: NsdServiceInfo) {
            Log.d("DeviceDiscovery", "Service found: $serviceInfo")
            nsdManager.resolveService(serviceInfo, resolveListener)
        }

        override fun onServiceLost(serviceInfo: NsdServiceInfo) {
            Log.e("DeviceDiscovery", "Service lost: $serviceInfo")
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Log.i("DeviceDiscovery", "Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e("DeviceDiscovery", "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e("DeviceDiscovery", "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }
    }

    private val resolveListener = object : NsdManager.ResolveListener {
        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            Log.d("DeviceDiscovery", "Service resolved: $serviceInfo")
            val device = DeviceInfo(
                name = serviceInfo.serviceName,
                host = serviceInfo.host.hostAddress ?: "",
                port = serviceInfo.port
            )
            if (!discoveredDevices.any { it.host == device.host }) {
                discoveredDevices.add(device)
                //mainViewModel.updateDiscoveredDevices(discoveredDevices)
            }
        }

        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            Log.e("DeviceDiscovery", "Resolve failed: $errorCode")
        }
    }

    fun startDiscovery() {
        // Acquire multicast lock
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        multicastLock = wifiManager.createMulticastLock("myMulticastLock")
        multicastLock?.setReferenceCounted(true)
        multicastLock?.acquire()

        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    fun stopDiscovery() {
        nsdManager.stopServiceDiscovery(discoveryListener)
        multicastLock?.release()
    }
}
