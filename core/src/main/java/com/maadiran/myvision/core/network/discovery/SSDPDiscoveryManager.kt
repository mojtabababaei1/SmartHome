package com.maadiran.myvision.core.network.discovery

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.maadiran.myvision.core.model.DeviceInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import java.net.*
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class SSDPDiscoveryManager(
    private val context: Context,
    private val coroutineScope: CoroutineScope
) {
    private val SSDP_ADDRESS = "239.255.255.250"
    private val SSDP_PORT = 1900

    private val _discoveredDevicesFlow = MutableSharedFlow<DeviceInfo>(extraBufferCapacity = 64)
    val discoveredDevicesFlow = _discoveredDevicesFlow.asSharedFlow()

    fun discoverDevices() {
        coroutineScope.launch(Dispatchers.IO) {
            val ssdpJob = launch { ssdpDiscovery() }
            val mdnsJob = launch { mdnsDiscovery() }
            ssdpJob.join()
            mdnsJob.join()
        }
    }

    private suspend fun ssdpDiscovery() {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val multicastLock = wifiManager.createMulticastLock("ssdpDiscovery")
        multicastLock.setReferenceCounted(true)
        multicastLock.acquire()

        try {
            val multicastSocket = MulticastSocket()
            multicastSocket.soTimeout = 5000
            val group = InetAddress.getByName(SSDP_ADDRESS)
            multicastSocket.joinGroup(group)

            val searchMessage = """
                M-SEARCH * HTTP/1.1
                HOST: $SSDP_ADDRESS:$SSDP_PORT
                MAN: "ssdp:discover"
                MX: 1
                ST: ssdp:all

            """.trimIndent()

            val searchBytes = searchMessage.toByteArray()
            val packet = DatagramPacket(searchBytes, searchBytes.size, group, SSDP_PORT)
            multicastSocket.send(packet)

            val buffer = ByteArray(2048)
            val receivePacket = DatagramPacket(buffer, buffer.size)

            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < 5000) {
                try {
                    multicastSocket.receive(receivePacket)
                    val response = String(receivePacket.data, 0, receivePacket.length)
                    Log.d("SSDPDiscovery", "Received: $response")

                    val location = parseHeader(response, "LOCATION")
                    if (location != null) {
                        val deviceInfo = fetchDeviceInfo(location, receivePacket.address.hostAddress)
                        if (deviceInfo != null) {
                            _discoveredDevicesFlow.emit(deviceInfo)
                        }
                    }
                } catch (e: SocketTimeoutException) {
                    break
                }
            }
            multicastSocket.leaveGroup(group)
            multicastSocket.close()
        } catch (e: Exception) {
            Log.e("SSDPDiscovery", "Error during SSDP discovery", e)
        } finally {
            multicastLock.release()
        }
    }

    private fun parseHeader(response: String, header: String): String? {
        val pattern = Pattern.compile("$header: (.*)", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(response)
        return if (matcher.find()) {
            matcher.group(1)?.trim()
        } else {
            null
        }
    }

    private suspend fun fetchDeviceInfo(location: String, hostAddress: String): DeviceInfo? {
        return try {
            val url = URL(location)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            val inputStream = connection.inputStream
            val friendlyName = parseDeviceDescription(inputStream)
            inputStream.close()
            DeviceInfo(
                name = friendlyName ?: "Unknown Device",
                host = hostAddress,
                port = if (url.port != -1) url.port else 80
            )
        } catch (e: Exception) {
            Log.e("SSDPDiscovery", "Error fetching device info", e)
            null
        }
    }

    private fun parseDeviceDescription(inputStream: InputStream): String? {
        try {
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var inFriendlyName = false
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName.equals("friendlyName", ignoreCase = true)) {
                            inFriendlyName = true
                        }
                    }
                    XmlPullParser.TEXT -> {
                        if (inFriendlyName) {
                            return parser.text
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (tagName.equals("friendlyName", ignoreCase = true)) {
                            inFriendlyName = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            Log.e("SSDPDiscovery", "Error parsing device description", e)
        }
        return null
    }

    private suspend fun mdnsDiscovery() {
        val MDNS_ADDRESS = "224.0.0.251"
        val MDNS_PORT = 5353
        val SERVICE_TYPE = "_androidtvremote2._tcp.local"
        val bufferSize = 2048

        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val multicastLock = wifiManager.createMulticastLock("mdnsDiscovery")
        multicastLock.setReferenceCounted(true)
        multicastLock.acquire()

        try {
            val multicastSocket = MulticastSocket(MDNS_PORT)
            multicastSocket.soTimeout = 5000
            multicastSocket.joinGroup(InetAddress.getByName(MDNS_ADDRESS))

            // Build the mDNS query message
            val queryMessage = buildMDNSQuery(SERVICE_TYPE)

            // Send the mDNS query
            val packet = DatagramPacket(queryMessage, queryMessage.size, InetAddress.getByName(MDNS_ADDRESS), MDNS_PORT)
            multicastSocket.send(packet)

            // Listen for responses
            val buffer = ByteArray(bufferSize)
            val receivePacket = DatagramPacket(buffer, buffer.size)
            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < 5000) {
                try {
                    multicastSocket.receive(receivePacket)
                    val data = receivePacket.data.copyOf(receivePacket.length)
                    val response = parseMDNSResponse(data)
                    if (response != null) {
                        _discoveredDevicesFlow.emit(response)
                    }
                } catch (e: SocketTimeoutException) {
                    break
                }
            }
            multicastSocket.leaveGroup(InetAddress.getByName(MDNS_ADDRESS))
            multicastSocket.close()
        } catch (e: Exception) {
            Log.e("MDNSDiscovery", "Error during mDNS discovery", e)
        } finally {
            multicastLock.release()
        }
        }
    }

    private fun buildMDNSQuery(serviceType: String): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val dataOutputStream = DataOutputStream(byteArrayOutputStream)

        // Header
        dataOutputStream.writeShort(0) // Transaction ID
        dataOutputStream.writeShort(0x0000) // Flags
        dataOutputStream.writeShort(0x0001) // Questions
        dataOutputStream.writeShort(0x0000) // Answer RRs
        dataOutputStream.writeShort(0x0000) // Authority RRs
        dataOutputStream.writeShort(0x0000) // Additional RRs

        // Question Section
        writeQName(dataOutputStream, serviceType)
        dataOutputStream.writeShort(0x000C) // QType PTR
        dataOutputStream.writeShort(0x0001) // QClass IN

        return byteArrayOutputStream.toByteArray()
    }

    private fun writeQName(outputStream: DataOutputStream, name: String) {
        val parts = name.split('.')
        for (part in parts) {
            val bytes = part.toByteArray(StandardCharsets.UTF_8)
            outputStream.writeByte(bytes.size)
            outputStream.write(bytes)
        }
        outputStream.writeByte(0) // End of name
    }

    private fun parseMDNSResponse(data: ByteArray): DeviceInfo? {
        try {
            val inputStream = ByteArrayInputStream(data)
            val dataInputStream = DataInputStream(inputStream)

            // Read header
            dataInputStream.readUnsignedShort() // Transaction ID
            dataInputStream.readUnsignedShort() // Flags
            val questions = dataInputStream.readUnsignedShort()
            val answerRRs = dataInputStream.readUnsignedShort()
            dataInputStream.readUnsignedShort() // Authority RRs
            dataInputStream.readUnsignedShort() // Additional RRs

            // Skip questions
            for (i in 0 until questions) {
                skipQName(dataInputStream)
                dataInputStream.readUnsignedShort() // QType
                dataInputStream.readUnsignedShort() // QClass
            }

            // Read answers
            for (i in 0 until answerRRs) {
                val name = readQName(dataInputStream, data)
                val type = dataInputStream.readUnsignedShort()
                dataInputStream.readUnsignedShort() // Class
                dataInputStream.readInt() // TTL
                val rdLength = dataInputStream.readUnsignedShort()

                if (type == 33) { // SRV record
                    val priority = dataInputStream.readUnsignedShort()
                    val weight = dataInputStream.readUnsignedShort()
                    val port = dataInputStream.readUnsignedShort()
                    val target = readQName(dataInputStream, data)

                    val deviceName = name.substringBefore("._androidtvremote2._tcp.local")
                    val deviceInfo = DeviceInfo(
                        name = deviceName,
                        host = target.trim('.'),
                        port = port
                    )
                    return deviceInfo
                } else {
                    dataInputStream.skipBytes(rdLength)
                }
            }
        } catch (e: Exception) {
            Log.e("MDNSDiscovery", "Error parsing mDNS response", e)
        }
        return null
    }

    private fun skipQName(dataInputStream: DataInputStream) {
        var length = dataInputStream.readUnsignedByte()
        while (length > 0) {
            if ((length and 0xC0) == 0xC0) {
                dataInputStream.readUnsignedByte()
                break
            } else {
                dataInputStream.skipBytes(length)
                length = dataInputStream.readUnsignedByte()
            }
        }
    }

    private fun readQName(dataInputStream: DataInputStream, data: ByteArray): String {
        val result = StringBuilder()
        var length = dataInputStream.readUnsignedByte()
        while (length > 0) {
            if ((length and 0xC0) == 0xC0) {
                val pointer = ((length and 0x3F) shl 8) or dataInputStream.readUnsignedByte()
                result.append(readNameFromPointer(pointer, data))
                break
            } else {
                val labelBytes = ByteArray(length)
                dataInputStream.readFully(labelBytes)
                val label = String(labelBytes)
                result.append(label).append('.')
                length = dataInputStream.readUnsignedByte()
            }
        }
        return result.toString()
    }

    private fun readNameFromPointer(pointer: Int, data: ByteArray): String {
        val result = StringBuilder()
        var offset = pointer
        var length = data[offset++].toInt() and 0xFF
        while (length > 0) {
            if ((length and 0xC0) == 0xC0) {
                val newPointer = ((length and 0x3F) shl 8) or (data[offset++].toInt() and 0xFF)
                result.append(readNameFromPointer(newPointer, data))
                break
            } else {
                val labelBytes = data.copyOfRange(offset, offset + length)
                val label = String(labelBytes)
                result.append(label).append('.')
                offset += length
                length = data[offset++].toInt() and 0xFF
            }
        }
        return result.toString()
    }