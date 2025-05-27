package com.maadiran.myvision.domain.services

import kotlinx.coroutines.flow.StateFlow

interface IVoiceServiceManager {
    val currentVoiceState: StateFlow<VoiceState>
    val recognitionText: StateFlow<String>

    fun initialize(onVoiceCommand: (Int) -> Unit, onUrlReceived: (String) -> Unit)
    fun startVoskVoice()
    fun startGoogleVoice()
    fun stopAllVoiceServices()
    fun destroy()
    fun startListening(onResult: (String) -> Unit, onError: () -> Unit)

}

sealed class VoiceState {
    object Idle : VoiceState()
    object VoskActive : VoiceState()
    object GoogleActive : VoiceState()
    data class Error(val message: String) : VoiceState()
    data class UrlReceived(val url: String) : VoiceState()
}
