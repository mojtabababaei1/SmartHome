package com.maadiran.myvision.presentation.features.devices.tv.ui.preview

import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.domain.services.VoiceState
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewVoiceServiceManager : IVoiceServiceManager {
    override val currentVoiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)
    override val recognitionText = MutableStateFlow("")

    override fun initialize(onVoiceCommand: (Int) -> Unit, onUrlReceived: (String) -> Unit) {}
    override fun startVoskVoice() {}
    override fun startGoogleVoice() {}
    override fun stopAllVoiceServices() {}
    override fun destroy() {}
    override fun startListening(onResult: (String) -> Unit, onError: () -> Unit) {
        // No-op for preview
    }

}
