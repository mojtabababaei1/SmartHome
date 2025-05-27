package com.maadiran.myvision.presentation.features.devices.tv.ui.preview

import com.maadiran.myvision.domain.tv.IPairingManager

class PreviewPairingManager : IPairingManager {
    override fun on(event: String, listener: () -> Unit) {}
    
    override suspend fun start(): Boolean = true
    
    override fun sendCode(code: String): Boolean = true
    
    override fun stop() {}
}
