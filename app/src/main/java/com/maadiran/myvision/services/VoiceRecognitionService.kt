// VoiceRecognitionService.kt
package com.maadiran.myvision.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.content.ContextCompat

class VoiceRecognitionService : Service(), RecognitionListener {

    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var recognizerIntent: Intent
    private lateinit var currentContext: String

    override fun onCreate() {
        super.onCreate()
        initializeSpeechRecognizer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentContext = intent?.getStringExtra("context") ?: "default"
        return START_STICKY
    }

    private fun initializeSpeechRecognizer() {
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("VoiceService", "Permission not granted")
            stopSelf()
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(this)

        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR") // Farsi language
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        startListening()
    }

    private fun startListening() {
        speechRecognizer?.startListening(recognizerIntent)
    }

    override fun onDestroy() {
        speechRecognizer?.destroy()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // RecognitionListener methods
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}

    override fun onError(error: Int) {
        Log.e("VoiceService", "Error: $error")
        // Handle insufficient permissions error
        if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
            Log.e("VoiceService", "Insufficient permissions")
            stopSelf()
            return
        }
        // Restart listening on other errors
        startListening()
    }

    override fun onResults(results: Bundle?) {
        processResults(results)
        // Restart listening after results
        startListening()
    }

    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}

    private fun processResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.forEach { result ->
            Log.d("VoiceService", "Recognized: $result")
            sendCommandToApp(result)
        }
    }

    private fun sendCommandToApp(command: String) {
        val intent = Intent("com.maadiran.myvision.VOICE_COMMAND").apply {
            putExtra("command", command)
            putExtra("context", currentContext)
        }
        sendBroadcast(intent)
    }
}
