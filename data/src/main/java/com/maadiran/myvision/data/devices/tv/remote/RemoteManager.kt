package com.maadiran.myvision.data.devices.tv.remote

import android.util.Log
import com.maadiran.myvision.core.security.utils.CertificateUtils
import com.maadiran.myvision.data.mapper.RemoteKeyCodeMapper
import com.maadiran.myvision.data.proto.pairing.RemoteDirection
import com.maadiran.myvision.data.proto.pairing.RemoteMessage
import com.maadiran.myvision.domain.model.RemoteKeyCode as DomainKeyCode  // Alias for domain model
import com.maadiran.myvision.data.proto.pairing.RemoteKeyCode as ProtoKeyCode  // Alias for protobuf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*
import java.security.cert.X509Certificate  // Change this import
import java.util.Arrays

class RemoteManager(
    private val host: String,
    private val port: Int,
    private val certs: Map<String, String>
) {
    // Add socket state management
    private val socketLock = Any()
    private var isConnected = false

    // Existing properties remain unchanged
    private val remoteMessageManager = RemoteMessageManager()
    private val listeners = mutableMapOf<String, MutableList<(Any?) -> Unit>>()
    private var clientSocket: SSLSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private val chunks = mutableListOf<Byte>()

    // Add connection state check
    private fun isSocketValid(): Boolean {
        return try {
            clientSocket?.let { socket ->
                socket.isConnected && !socket.isClosed && isConnected
            } ?: false
        } catch (e: Exception) {
            Log.e("RemoteManager", "Error checking socket validity", e)
            false
        }
    }

    private suspend fun attemptConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            val sslContext = createSSLContext(certs)
            val socketFactory = sslContext.socketFactory
            clientSocket = (socketFactory.createSocket(host, port) as SSLSocket).apply {
                enabledProtocols = arrayOf("TLSv1.2")
                // Use all available cipher suites
                enabledCipherSuites = supportedCipherSuites
                soTimeout = 5000
                useClientMode = true
                
                // Disable hostname verification
                sslParameters = SSLParameters().apply {
                    endpointIdentificationAlgorithm = null
                }
                
                try {
                    Log.d("RemoteManager", "Starting SSL handshake...")
                    startHandshake()
                    Log.d("RemoteManager", "SSL handshake completed successfully")
                } catch (e: Exception) {
                    Log.e("RemoteManager", "SSL handshake failed", e)
                    throw e
                }
            }
            true
        } catch (e: Exception) {
            Log.e("RemoteManager", "Connection attempt failed: ${e.message}", e)
            false
        }
    }

    fun on(event: String, listener: (Any?) -> Unit) {
        listeners.getOrPut(event) { mutableListOf() }.add(listener)
    }

    private fun emit(event: String, data: Any? = null) {
        listeners[event]?.forEach { it(data) }
    }
    private var messageReadingJob: Job? = null

    suspend fun start(): Boolean = withContext(Dispatchers.IO) {
        synchronized(socketLock) {
            try {
                if (isSocketValid()) {
                    return@withContext true
                }

                stop()

                val sslContext = createSSLContext(certs)
                val socketFactory = sslContext.socketFactory
                clientSocket = socketFactory.createSocket(host, port) as SSLSocket
                clientSocket?.startHandshake()

                outputStream = clientSocket?.outputStream
                inputStream = clientSocket?.inputStream

                isConnected = true

                // Store the job reference
                messageReadingJob = CoroutineScope(Dispatchers.IO).launch {
                    readIncomingMessages()
                }

                return@withContext true
            } catch (e: Exception) {
                Log.e("RemoteManager", "Error starting connection: ${e.message}")
                isConnected = false
                return@withContext false
            }
        }
    }

    fun stop() {
        synchronized(socketLock) {
            try {
                messageReadingJob?.cancel() // Cancel the reading job
                messageReadingJob = null
                isConnected = false
                inputStream?.close()
                outputStream?.close()
                clientSocket?.close()
            } catch (e: Exception) {
                Log.e("RemoteManager", "Error closing connection", e)
            } finally {
                inputStream = null
                outputStream = null
                clientSocket = null
            }
        }
    }

    private suspend fun readIncomingMessages() {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (clientSocket?.isConnected == true) {
                bytesRead = inputStream?.read(buffer) ?: -1
                if (bytesRead > 0) {
                    val receivedData = buffer.copyOf(bytesRead)
                    handleIncomingData(receivedData)
                }
            }
        }
    }

    private fun handleIncomingData(data: ByteArray) {
        // Accumulate data
        chunks.addAll(data.toList())

        // Check if we have a complete message
        if (chunks.isNotEmpty() && chunks[0].toInt() == chunks.size - 1) {
            val messageData = chunks.subList(1, chunks.size).toByteArray()
            val message = remoteMessageManager.parse(messageData)

            Log.d("RemoteManager", "Received: ${message.toString()}")

            when {
                message.hasRemoteConfigure() -> {
                    val configureResponse = remoteMessageManager.createRemoteConfigure()
                    CoroutineScope(Dispatchers.IO).launch {
                        sendMessage(configureResponse)
                    }
                    emit("ready")
                }
                message.hasRemoteSetActive() -> {
                    val setActiveResponse = remoteMessageManager.createRemoteSetActive(622)
                    CoroutineScope(Dispatchers.IO).launch {
                        sendMessage(setActiveResponse)
                    }
                }
                message.hasRemotePingRequest() -> {
                    val pingResponse = remoteMessageManager.createRemotePingResponse(message.remotePingRequest.val1)
                    CoroutineScope(Dispatchers.IO).launch {
                        sendMessage(pingResponse)
                    }
                }
                message.hasRemoteImeKeyInject() -> {
                    emit("current_app", message.remoteImeKeyInject.appInfo.appPackage)
                }
                message.hasRemoteStart() -> {
                    emit("powered", message.remoteStart.started)
                }
                message.hasRemoteSetVolumeLevel() -> {
                    val volumeData = mapOf(
                        "level" to message.remoteSetVolumeLevel.volumeLevel,
                        "maximum" to message.remoteSetVolumeLevel.volumeMax,
                        "muted" to message.remoteSetVolumeLevel.volumeMuted
                    )
                    emit("volume", volumeData)
                }
                message.hasRemoteError() -> {
                    emit("error", message.remoteError)
                }
                else -> {
                    Log.d("RemoteManager", "Unknown message received")
                }
            }
            // Clear chunks
            chunks.clear()
        }
    }

    fun sendPower() {
        val message = remoteMessageManager.createRemoteKeyInject(
            RemoteDirection.SHORT, 
            ProtoKeyCode.KEYCODE_POWER
        )
        CoroutineScope(Dispatchers.IO).launch {
            sendMessage(message)
        }
    }

    fun sendKey(keyCode: DomainKeyCode) {
        val protoKeyCode = RemoteKeyCodeMapper.mapToProto(keyCode)
        val message = remoteMessageManager.createRemoteKeyInject(
            RemoteDirection.SHORT,  // Default direction
            protoKeyCode
        )
        CoroutineScope(Dispatchers.IO).launch {
            sendMessage(message)
        }
    }

    fun sendAppLink(appLink: String) {
        Log.d("RemoteManager", "Sending app link: $appLink")
        val message = remoteMessageManager.createRemoteAppLinkLaunchRequest(appLink)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                sendMessage(message)
                Log.d("RemoteManager", "Successfully sent app link message")
            } catch (e: Exception) {
                Log.e("RemoteManager", "Failed to send app link", e)
                throw e  // Propagate the error up
            }
        }
    }

    // Modify sendMessage to handle connection state
    private suspend fun sendMessage(message: RemoteMessage) = withContext(Dispatchers.IO) {
        // Check connection state outside synchronized block
        if (!isSocketValid()) {
            // Reconnect outside the synchronized block
            if (!start()) {
                throw IOException("Failed to establish connection")
            }
        }

        synchronized(socketLock) {
            try {
                // Double-check connection state after acquiring lock
                if (!isSocketValid()) {
                    throw IOException("Connection lost while waiting for lock")
                }

                val data = message.toByteArray()
                val length = data.size
                val output = ByteArray(length + 1)
                output[0] = length.toByte()
                System.arraycopy(data, 0, output, 1, length)

                outputStream?.let { stream ->
                    stream.write(output)
                    stream.flush()
                } ?: throw IOException("Output stream is null")

            } catch (e: Exception) {
                Log.e("RemoteManager", "Error sending message", e)
                isConnected = false
                throw e
            }
        }
    }

    private fun createSSLContext(certs: Map<String, String>): SSLContext {
        try {
            val sslContext = SSLContext.getInstance("TLSv1.2")
            
            // Create a more permissive trust manager
            val trustManager = object : X509TrustManager {
                @Throws(java.security.cert.CertificateException::class)
                override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {
                    Log.d("RemoteManager", "Checking client certificate: ${chain.firstOrNull()?.subjectDN}")
                    // Accept all clients
                }

                @Throws(java.security.cert.CertificateException::class)
                override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {
                    Log.d("RemoteManager", "Checking server certificate: ${chain.firstOrNull()?.subjectDN}")
                    // Accept all servers
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
            }

            // Load client key and certificate
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                load(null, null)
                val clientKey = CertificateUtils.loadPrivateKey(certs["key"] 
                    ?: throw IllegalStateException("Client key not found"))
                val clientCert = CertificateUtils.loadCertificate(certs["cert"] 
                    ?: throw IllegalStateException("Client certificate not found"))
                
                Log.d("RemoteManager", "Loading client certificate: ${clientCert.subjectDN}")
                setKeyEntry("client", clientKey, null, arrayOf(clientCert))
            }

            // Setup key manager
            val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore, null)
            }

            // Initialize SSLContext with our custom trust manager
            sslContext.init(
                keyManagerFactory.keyManagers,
                arrayOf(trustManager),
                SecureRandom()
            )

            return sslContext
        } catch (e: Exception) {
            Log.e("RemoteManager", "Error creating SSL context: ${e.message}", e)
            throw e
        }
    }
}