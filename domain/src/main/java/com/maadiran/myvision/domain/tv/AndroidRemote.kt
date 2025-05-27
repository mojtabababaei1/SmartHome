package com.maadiran.myvision.domain.tv

import android.util.Log
import com.maadiran.myvision.core.security.CertificateGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.maadiran.myvision.domain.model.RemoteKeyCode
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLContext

class AndroidRemote(
    private val host: String,
    options: Options,
    private val isPaired: Boolean,
    private val pairingManagerFactory: (String, Int, Map<String, String>, String) -> IPairingManager,
    private val remoteManagerFactory: (String, Int, Map<String, String>) -> IRemoteManager
) {
    data class Options(
        val cert: Map<String, String>? = null,
        val pairingPort: Int = 6467,
        val remotePort: Int = 6466,
        val serviceName: String = "Service Name"
    )

    private val cert: MutableMap<String, String> = options.cert?.toMutableMap() ?: mutableMapOf()
    private val pairingPort = options.pairingPort
    private val remotePort = options.remotePort
    private val serviceName = options.serviceName

    private lateinit var pairingManager: IPairingManager
    private lateinit var remoteManager: IRemoteManager

    private val listeners = mutableMapOf<String, MutableList<(Any?) -> Unit>>()

    fun on(event: String, listener: (Any?) -> Unit) {
        listeners.getOrPut(event) { mutableListOf() }.add(listener)
    }

    private fun emit(event: String, data: Any? = null) {
        listeners[event]?.forEach { it(data) }
    }

    suspend fun start(): Boolean = withContext(Dispatchers.IO) {
        try {
            // Generate certificates if not provided
            if (cert.isEmpty() || !cert.containsKey("key") || !cert.containsKey("cert")) {
                val generatedCert = CertificateGenerator.generateFull(
                    serviceName,
                    "CNT",
                    "ST",
                    "LOC",
                    "O",
                    "OU"
                )
                cert.putAll(generatedCert)
                
                // Log certificate generation
                Log.d("AndroidRemote", "Generated new certificates: ${cert.keys}")
            }

            if (!isPaired) {
                // Proceed with pairing
                pairingManager = pairingManagerFactory(
                    host,
                    pairingPort,
                    cert,
                    serviceName
                )
                pairingManager.on("secret") {
                    emit("secret")
                }

                val paired = pairingManager.start()
                if (!paired) return@withContext false
            }

            // Initialize RemoteManager
            remoteManager = remoteManagerFactory(host, remotePort, cert)
            remoteManager.on("powered") { data -> emit("powered", data) }
            remoteManager.on("volume") { data -> emit("volume", data) }
            remoteManager.on("current_app") { data -> emit("current_app", data) }
            remoteManager.on("ready") { emit("ready") }
            remoteManager.on("unpaired") { emit("unpaired") }

            val started = remoteManager.start()
            return@withContext started
            
        } catch (e: Exception) {
            Log.e("AndroidRemote", "Error in start: ${e.message}", e)
            return@withContext false
        }
    }

    suspend fun sendCode(code: String): Boolean {
        return pairingManager.sendCode(code)
    }

    fun sendPower() {
        remoteManager.sendPower()
    }

    fun sendAppLink(appLink: String) {
        remoteManager.sendAppLink(appLink)
    }

    fun sendKey(keyCode: RemoteKeyCode) {  // Updated to use domain RemoteKeyCode
        remoteManager.sendKey(keyCode)
    }

    fun stop() {
        remoteManager.stop()
    }

    private fun createSSLContext(cert: Map<String, String>) {
        // Implement SSL context creation logic here
    }
}