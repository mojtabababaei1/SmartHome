package com.maadiran.myvision.data.services

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.maadiran.myvision.data.devices.tv.voice.FarsiVoiceRecognitionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class GoogleVoiceService(
    private val context: Context,
    private val onUrlReceived: (String) -> Unit,
    private val onTranscriptionUpdate: (String) -> Unit
) : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var speechRecognizer: SpeechRecognizer? = null
    private var voiceService: FarsiVoiceRecognitionService? = null
    private val client = OkHttpClient()
    private val TAG = "GoogleVoiceService"

    private val _recognitionState =
        MutableStateFlow<VoiceRecognitionState>(VoiceRecognitionState.Idle)
    val recognitionState: StateFlow<VoiceRecognitionState> = _recognitionState

    sealed class VoiceRecognitionState {
        object Idle : VoiceRecognitionState()
        object Listening : VoiceRecognitionState()
        data class TranscriptionReceived(val text: String) : VoiceRecognitionState()
        data class WaitingForConfirmation(val text: String) : VoiceRecognitionState()
        data class ProcessingRequest(val text: String) : VoiceRecognitionState()
        data class Error(val message: String) : VoiceRecognitionState()
        data class UrlReceived(val url: String) : VoiceRecognitionState()
    }

    init {
        initializeGoogleVoice()
    }

    private fun initializeGoogleVoice() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(createRecognitionListener())
        }
    }

    private fun createRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
                    if (matches.isNotEmpty()) {
                        val transcription = matches[0]
                        _recognitionState.value =
                            VoiceRecognitionState.TranscriptionReceived(transcription)
                        onTranscriptionUpdate(transcription)
                        // Direct send to server without confirmation
                        sendToFlaskServer(transcription)
                    }
                }
            }


            override fun onPartialResults(partialResults: Bundle?) {
                partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
                    if (matches.isNotEmpty()) {
                        val partialText = matches[0]
                        onTranscriptionUpdate(partialText)
                    }
                }
            }

            override fun onError(error: Int) {
                Log.e(TAG, "Recognition error: $error")
                _recognitionState.value =
                    VoiceRecognitionState.Error("Voice recognition error: $error")
                onTranscriptionUpdate("")
            }

            override fun onReadyForSpeech(params: Bundle?) {
                _recognitionState.value = VoiceRecognitionState.Listening
                onTranscriptionUpdate("در حال گوش دادن...")
            }

            override fun onEndOfSpeech() {
                // Optional: Handle end of speech
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Optional: Handle audio level changes
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Optional: Handle buffer received
            }

            override fun onBeginningOfSpeech() {
                // Optional: Handle beginning of speech
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Optional: Handle other events
            }
        }
    }

    private fun createConfirmationListener(transcription: String): RecognitionListener {
        return object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
                    if (matches.isNotEmpty()) {
                        val confirmation = matches[0].trim().lowercase()
                        if (confirmation == "بله" || confirmation == "آره") {
                            onTranscriptionUpdate("در حال جستجو...")
                            sendToFlaskServer(transcription)
                        } else {
                            _recognitionState.value = VoiceRecognitionState.Idle
                            onTranscriptionUpdate("")
                            startListening(voiceService)
                        }
                    }
                }
            }
            override fun onError(error: Int) {
                Log.e(TAG, "Confirmation error: $error")
                _recognitionState.value = VoiceRecognitionState.Error("Confirmation error: $error")
                onTranscriptionUpdate("خطا در تایید دستور")
                startListening(voiceService)
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onEndOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }
    fun startListening(currentVoiceService: FarsiVoiceRecognitionService?) {
        // First ensure Vosk is stopped
        currentVoiceService?.stopListening()
        currentVoiceService?.destroy()  // Add this to fully stop Vosk

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        try {
            _recognitionState.value = VoiceRecognitionState.Listening
            onTranscriptionUpdate("در حال گوش دادن...")
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start listening", e)
            _recognitionState.value =
                VoiceRecognitionState.Error("Failed to start listening: ${e.message}")
            onTranscriptionUpdate("خطا در شروع تشخیص صدا")
        }
    }


    private fun sendToFlaskServer(text: String) {
        _recognitionState.value = VoiceRecognitionState.ProcessingRequest(text)
        onTranscriptionUpdate("در حال پردازش درخواست...")

        launch(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("text", text)
                    put("suggestion", "Suggestion 1")
                }
                val requestBody = json.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaType())

                val request = Request.Builder()
                    .url(FLASK_SERVER_URL)
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected response $response")
                    }

                    val responseJson = JSONObject(response.body!!.string())
                    val url = responseJson.getString("url")

                    Log.d(TAG, "Received URL from server: $url") // Add logging

                    withContext(Dispatchers.Main) {
                        _recognitionState.value = VoiceRecognitionState.UrlReceived(url)
                        onTranscriptionUpdate("لینک دریافت شد")
                        onUrlReceived(url)  // This callback needs to be properly implemented
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Server error", e)
                withContext(Dispatchers.Main) {
                    _recognitionState.value =
                        VoiceRecognitionState.Error("Server error: ${e.message}")
                    onTranscriptionUpdate("خطا در ارتباط با سرور")
                }
            }
        }
    }
    fun stopListening() {
        speechRecognizer?.stopListening()
        _recognitionState.value = VoiceRecognitionState.Idle
        onTranscriptionUpdate("")
    }

    fun destroy() {
        job.cancel()
        speechRecognizer?.destroy()
        speechRecognizer = null
        onTranscriptionUpdate("")
    }

    companion object {
        private const val FLASK_SERVER_URL = "http://78.39.129.10:80/search"
    }
}