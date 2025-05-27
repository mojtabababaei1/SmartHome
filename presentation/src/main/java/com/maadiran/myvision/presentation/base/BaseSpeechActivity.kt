package com.maadiran.myvision.presentation.base

import androidx.activity.ComponentActivity
import com.maadiran.myvision.core.utils.GoogleSpeechRecognizer
import javax.inject.Inject

abstract class BaseSpeechActivity : ComponentActivity() {
    @Inject
    lateinit var googleSpeechRecognizer: GoogleSpeechRecognizer

    private val speechRecognizerLauncher = registerForActivityResult(
        GoogleSpeechRecognizer.GoogleSpeechContract()
    ) { result ->
        handleSpeechResult(result)
    }

    protected abstract fun handleSpeechResult(result: Result<String>)

    protected fun startSpeechRecognition() {
        speechRecognizerLauncher.launch(Unit)
    }
}