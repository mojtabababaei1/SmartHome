package com.maadiran.myvision.data.devices.tv

import com.maadiran.myvision.data.devices.tv.remote.RemoteManager
import com.maadiran.myvision.domain.model.RemoteKeyCode
import com.maadiran.myvision.domain.tv.IRemoteManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteManagerImpl @Inject constructor() : IRemoteManager {
    private var remoteManager: RemoteManager? = null

    override fun initialize(host: String, port: Int, certificates: Map<String, String>) {
        remoteManager = RemoteManager(host, port, certificates)
    }

    override suspend fun start(): Boolean {
        return remoteManager?.start() ?: false
    }

    override fun stop() {
        remoteManager?.stop()
    }

    override fun on(event: String, listener: (Any?) -> Unit) {
        remoteManager?.on(event, listener)
    }

    override fun sendPower() {
        remoteManager?.sendPower()
    }

    override fun sendKey(keyCode: RemoteKeyCode) {
        remoteManager?.sendKey(keyCode)
    }

    override fun sendAppLink(appLink: String) {
        remoteManager?.sendAppLink(appLink)
    }
}
