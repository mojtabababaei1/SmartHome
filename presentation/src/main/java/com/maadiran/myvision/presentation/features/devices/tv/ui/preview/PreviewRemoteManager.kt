package com.maadiran.myvision.presentation.features.devices.tv.ui.preview

import com.maadiran.myvision.domain.model.RemoteKeyCode
import com.maadiran.myvision.domain.tv.IRemoteManager

class PreviewRemoteManager : IRemoteManager {
    override fun initialize(host: String, port: Int, certificates: Map<String, String>) {
        // Preview implementation
    }

    override suspend fun start(): Boolean = true

    override fun stop() {
        // Preview implementation
    }

    override fun on(event: String, listener: (Any?) -> Unit) {
        // Preview implementation
    }

    override fun sendPower() {
        // Preview implementation
    }

    override fun sendKey(keyCode: RemoteKeyCode) {
        // Preview implementation
    }

    override fun sendAppLink(appLink: String) {
        // Preview implementation
    }
}
