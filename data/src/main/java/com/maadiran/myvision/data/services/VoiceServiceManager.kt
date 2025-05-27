package com.maadiran.myvision.data.services


import android.content.Context
import android.util.Log
import com.maadiran.myvision.data.devices.tv.voice.FarsiVoiceRecognitionService
import com.maadiran.myvision.data.mapper.RemoteKeyCodeMapper
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.domain.services.VoiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

class VoiceServiceManager @Inject constructor(
    private val context: Context
) : IVoiceServiceManager {
    private var farsiVoiceService: FarsiVoiceRecognitionService? = null
    private var googleVoiceService: GoogleVoiceService? = null
    private var isInitialized = false
    private var onUrlReceivedCallback: ((String) -> Unit)? = null

    private val _currentVoiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)
    override val currentVoiceState: StateFlow<VoiceState> = _currentVoiceState

    private val _recognitionText = MutableStateFlow<String>("")
    override val recognitionText: StateFlow<String> = _recognitionText

    override fun initialize(
        onVoiceCommand: (Int) -> Unit,
        onUrlReceived: (String) -> Unit
    ) {
        try {
            if (!isInitialized) {
                Log.d(TAG, "Initializing voice services")
                cleanupModelFiles()

                onUrlReceivedCallback = onUrlReceived

                farsiVoiceService = FarsiVoiceRecognitionService(
                    context = context,
                    onCommand = { keyCode ->
                        Log.d(TAG, "Vosk command received: $keyCode")
                        // Convert domain RemoteKeyCode to proto RemoteKeyCode and get its value
                        val protoKeyCode = RemoteKeyCodeMapper.mapToProto(keyCode)
                        onVoiceCommand(protoKeyCode.number)
                    }
                )

                googleVoiceService = GoogleVoiceService(
                    context = context,
                    onUrlReceived = { url ->
                        Log.d(TAG, "Google voice URL received: $url")
                        handleUrlReceived(url)
                    },
                    onTranscriptionUpdate = { text ->
                        Log.d(TAG, "Transcription update: $text")
                        _recognitionText.value = text
                    }
                )

                isInitialized = true
                _currentVoiceState.value = VoiceState.Idle
                Log.d(TAG, "Voice services initialized successfully")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing voice services", e)
            _currentVoiceState.value =
                VoiceState.Error("Failed to initialize voice services: ${e.message}")
        }
    }
    override fun startListening(
        onResult: (String) -> Unit,
        onError: () -> Unit
    ) {
        try {
            Log.d(TAG, "Starting voice listening...")
            googleVoiceService?.startListening(null)

        } catch (e: Exception) {
            Log.e(TAG, "Error starting voice listening", e)
            onError()
        }
    }




    private fun handleUrlReceived(url: String) {
        try {
            Log.d(TAG, "Handling received URL: $url")
            _currentVoiceState.value = VoiceState.UrlReceived(url)
            onUrlReceivedCallback?.let { callback ->
                Log.d(TAG, "Invoking URL callback")
                callback(url)
            } ?: run {
                Log.e(TAG, "URL callback is null")
                _currentVoiceState.value = VoiceState.Error("URL callback not initialized")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling URL", e)
            _currentVoiceState.value = VoiceState.Error("Failed to handle URL: ${e.message}")
        }
    }

    override fun startVoskVoice() {
        try {
            Log.d(TAG, "Starting Vosk voice recognition")

            if (farsiVoiceService == null) {
                Log.e(TAG, "farsiVoiceService is NULL")
                _currentVoiceState.value = VoiceState.Error("Farsi voice service is not initialized")
                return
            }

            googleVoiceService?.stopListening()

            _currentVoiceState.value = VoiceState.VoskActive

            Log.d(TAG, "Initializing Vosk...")
            farsiVoiceService?.initializeVosk()

            Log.d(TAG, "Starting to listen with Vosk...")
            farsiVoiceService?.startListening()

        } catch (e: Exception) {
            Log.e(TAG, "Error starting Vosk voice", e)
            _currentVoiceState.value = VoiceState.Error("Failed to start Vosk voice: ${e.message}")
        }
    }


    override fun startGoogleVoice() {
        try {
            if (!isInitialized) {
                Log.e(TAG, "Voice services not initialized")
                _currentVoiceState.value = VoiceState.Error("Voice services not initialized")
                return
            }

            Log.d(TAG, "Starting Google voice recognition")
            // First properly stop Vosk
            farsiVoiceService?.stopListening()
            farsiVoiceService?.destroy() // Add full cleanup

            _currentVoiceState.value = VoiceState.GoogleActive
            googleVoiceService?.startListening(null)  // Pass null instead of farsiVoiceService
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Google voice", e)
            _currentVoiceState.value =
                VoiceState.Error("Failed to start Google voice: ${e.message}")
        }
    }

    override fun stopAllVoiceServices() {
        Log.d(TAG, "Stopping all voice services")
        try {
            farsiVoiceService?.stopListening()
            farsiVoiceService?.destroy() // Make sure to destroy
            googleVoiceService?.stopListening()
            _currentVoiceState.value = VoiceState.Idle
            _recognitionText.value = ""
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping voice services", e)
            _currentVoiceState.value =
                VoiceState.Error("Failed to stop voice services: ${e.message}")
        }
    }

    override fun destroy() {
        Log.d(TAG, "Destroying voice services")
        try {
            stopAllVoiceServices()
            farsiVoiceService?.destroy()
            googleVoiceService?.destroy()
            farsiVoiceService = null
            googleVoiceService = null
            isInitialized = false
            _recognitionText.value = ""
            onUrlReceivedCallback = null
            cleanupModelFiles()
        } catch (e: Exception) {
            Log.e(TAG, "Error destroying voice services", e)
        }
    }

    private fun cleanupModelFiles() {
        try {
            val cacheDir = context.cacheDir
            val modelDir = File(cacheDir, "vosk-model-small-fa-0.42")
            if (modelDir.exists()) {
                modelDir.deleteRecursively()
                Log.d(TAG, "Cleaned up existing model files")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up model files", e)
        }
    }

    companion object {
        private const val TAG = "VoiceServiceManager"
    }
}