package com.maadiran.myvision.data.devices.tv.pairing

import android.util.Log
import com.maadiran.myvision.core.security.utils.CertificateUtils
import com.maadiran.myvision.data.proto.pairing.PairingMessage
import com.maadiran.myvision.domain.tv.IPairingManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import javax.net.ssl.*

class PairingManager(
    private val host: String,
    private val port: Int,
    private val certs: Map<String, String>,
    private val serviceName: String
) : IPairingManager {
    private val pairingMessageManager =
        PairingMessageManager()
    private val listeners = mutableMapOf<String, MutableList<() -> Unit>>()
    private var clientSocket: SSLSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun on(event: String, listener: () -> Unit) {
        listeners.getOrPut(event) { mutableListOf() }.add(listener)
    }

    private fun emit(event: String) {
        listeners[event]?.forEach { it() }
    }

    override suspend fun start(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val sslContext = createSSLContext(certs)
                val socketFactory = sslContext.socketFactory
                clientSocket = socketFactory.createSocket(host, port) as SSLSocket

                clientSocket?.enabledCipherSuites = clientSocket?.supportedCipherSuites
                clientSocket?.startHandshake()

                outputStream = clientSocket?.outputStream
                inputStream = clientSocket?.inputStream

                // Send Pairing Request
                val pairingRequest = pairingMessageManager.createPairingRequest(serviceName)
                sendMessage(pairingRequest)

                // Start reading incoming messages in the coroutine scope
                scope.launch {
                    readIncomingMessages()
                }

                true
            } catch (e: Exception) {
                Log.e("PairingManager", "Error during SSL handshake", e)
                false
            }
        }
    }

    private suspend fun readIncomingMessages() {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            var tempBuffer = ByteArray(0)

            while (clientSocket?.isConnected == true && isActive) {
                try {
                    bytesRead = inputStream?.read(buffer) ?: -1
                    if (bytesRead > 0) {
                        val receivedData = buffer.copyOf(bytesRead)
                        tempBuffer += receivedData

                        while (tempBuffer.isNotEmpty()) {
                            val length = tempBuffer[0].toInt() and 0xFF
                            if (tempBuffer.size >= length + 1) {
                                val messageData = tempBuffer.copyOfRange(1, length + 1)
                                handleIncomingMessage(messageData)
                                tempBuffer = tempBuffer.copyOfRange(length + 1, tempBuffer.size)
                            } else {
                                // Wait for more data
                                break
                            }
                        }
                    } else if (bytesRead == -1) {
                        // End of stream
                        break
                    }
                } catch (e: IOException) {
                    if (clientSocket?.isClosed == true) {
                        // Socket was closed, exit loop
                        break
                    } else {
                        Log.e("PairingManager", "Error reading from socket", e)
                        break
                    }
                }
            }
        }
    }

    private fun handleIncomingMessage(messageData: ByteArray) {
        try {
            val message = pairingMessageManager.parse(messageData)
            Log.d("PairingManager", "Received: ${message.toString()}")

            when {
                message.hasPairingRequestAck() -> {
                    val pairingOption = pairingMessageManager.createPairingOption()
                    sendMessage(pairingOption)
                }
                message.hasPairingOption() -> {
                    val pairingConfiguration = pairingMessageManager.createPairingConfiguration()
                    sendMessage(pairingConfiguration)
                }
                message.hasPairingConfigurationAck() -> {
                    emit("secret")
                }
                message.hasPairingSecretAck() -> {
                    Log.d("PairingManager", "Paired!")
                    scope.cancel() // Cancel the coroutine scope
                    clientSocket?.close()
                    // Proceed to initialize the remote manager if needed
                }
                message.status == PairingMessage.Status.STATUS_BAD_SECRET -> {
                    Log.e("PairingManager", "Received STATUS_BAD_SECRET from server.")
                    // Handle bad secret (e.g., notify user, retry, etc.)
                }
                else -> {
                    Log.d("PairingManager", "Unknown message received")
                }
            }
        } catch (e: Exception) {
            Log.e("PairingManager", "Error parsing message", e)
        }
    }

    override fun sendCode(code: String): Boolean {
        Log.d("PairingManager", "Sending code: $code")

        try {
            // Convert code from hexadecimal string to bytes
            val codeBytes = hexStringToByteArray(code)

            // Get codeForHash by removing first two characters
            val codeForHash = code.substring(2)
            val codeForHashBytes = hexStringToByteArray(codeForHash)

            // Get client and server certificates
            val session = clientSocket?.session
            val clientCertificate = session?.localCertificates?.firstOrNull() as? X509Certificate
            val serverCertificate = session?.peerCertificates?.firstOrNull() as? X509Certificate

            if (clientCertificate == null || serverCertificate == null) {
                Log.e("PairingManager", "Certificates not available in SSL session.")
                clientSocket?.close()
                return false
            }

            // Extract modulus and exponent from certificates
            val clientPublicKey = clientCertificate.publicKey as RSAPublicKey
            val serverPublicKey = serverCertificate.publicKey as RSAPublicKey

            val clientModulusHex = clientPublicKey.modulus.toString(16)
            var clientExponentHex = clientPublicKey.publicExponent.toString(16)
            clientExponentHex = "0" + clientExponentHex

            val serverModulusHex = serverPublicKey.modulus.toString(16)
            var serverExponentHex = serverPublicKey.publicExponent.toString(16)
            serverExponentHex = "0" + serverExponentHex

            // Convert hex strings to byte arrays
            val clientModulusBytes = hexStringToByteArray(clientModulusHex)
            val clientExponentBytes = hexStringToByteArray(clientExponentHex)
            val serverModulusBytes = hexStringToByteArray(serverModulusHex)
            val serverExponentBytes = hexStringToByteArray(serverExponentHex)

            // Log data for debugging
            Log.d("PairingManager", "Client Modulus Hex: $clientModulusHex")
            Log.d("PairingManager", "Client Exponent Hex Padded: $clientExponentHex")
            Log.d("PairingManager", "Server Modulus Hex: $serverModulusHex")
            Log.d("PairingManager", "Server Exponent Hex Padded: $serverExponentHex")
            Log.d("PairingManager", "Code For Hash: $codeForHash")

            // Compute hash as per the protocol
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(clientModulusBytes)
            digest.update(clientExponentBytes)
            digest.update(serverModulusBytes)
            digest.update(serverExponentBytes)
            digest.update(codeForHashBytes)
            val hash = digest.digest()

            // Log computed hash
            Log.d("PairingManager", "Computed Hash: ${hash.toHexString()}")

            // Compare first byte of hash with first byte of codeBytes
            if (hash[0] != codeBytes[0]) {
                Log.e("PairingManager", "Bad Code")
                clientSocket?.close()
                return false
            }

            // Send the pairing secret
            val pairingSecretMessage = pairingMessageManager.createPairingSecret(hash)
            sendMessage(pairingSecretMessage)
            return true

        } catch (e: Exception) {
            Log.e("PairingManager", "Error computing hash", e)
            return false
        }
    }

    override fun stop() {
        scope.cancel() // Cancel the coroutine scope
        try {
            inputStream?.close()
            outputStream?.close()
            clientSocket?.close()
        } catch (e: Exception) {
            Log.e("PairingManager", "Error closing resources", e)
        } finally {
            inputStream = null
            outputStream = null
            clientSocket = null
        }
    }

    // Helper function to convert hex string to byte array
    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            val byte = s.substring(i, i + 2).toInt(16).toByte()
            data[i / 2] = byte
        }
        return data
    }


    // Helper extension function to convert ByteArray to hex string
    fun ByteArray.toHexString(): String {
        return this.joinToString("") { "%02X".format(it) }
    }


    private fun sendMessage(message: PairingMessage) {
        val data = message.toByteArray()
        val length = data.size
        if (length > 255) {
            Log.e("PairingManager", "Message too long for 1-byte length prefix")
            return
        }
        val output = ByteArray(length + 1)
        output[0] = length.toByte()
        System.arraycopy(data, 0, output, 1, length)
        outputStream?.write(output)
        outputStream?.flush()
    }

    private fun createSSLContext(certs: Map<String, String>): SSLContext {
        val sslContext = SSLContext.getInstance("TLS")

        // Load client key and certificate
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)

        // Convert PEM keys to KeyStore entries
        val clientKey = CertificateUtils.loadPrivateKey(certs["key"]!!)
        val clientCert = CertificateUtils.loadCertificate(certs["cert"]!!)

        // Add logging
        Log.d("PairingManager", "Client Key: $clientKey")
        Log.d("PairingManager", "Client Cert: $clientCert")

        keyStore.setKeyEntry("client", clientKey, null, arrayOf(clientCert))

        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, null)

        // Use TrustAllCerts if serverCert is not available
        val trustManagers = if (certs.containsKey("serverCert")) {
            val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
            trustStore.load(null, null)

            val serverCert = CertificateUtils.loadCertificate(certs["serverCert"]!!)
            trustStore.setCertificateEntry("server", serverCert)

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(trustStore)
            trustManagerFactory.trustManagers
        } else {
            // For testing purposes, trust all certificates
            arrayOf<TrustManager>(TrustAllCerts())
        }

        sslContext.init(keyManagerFactory.keyManagers, trustManagers, SecureRandom())
        return sslContext
    }

    inner class TrustAllCerts : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

}
