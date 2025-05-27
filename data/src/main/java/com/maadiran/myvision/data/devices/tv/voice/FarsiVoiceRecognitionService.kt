package com.maadiran.myvision.data.devices.tv.voice

import android.content.Context
import android.util.Log
import com.maadiran.myvision.domain.model.RemoteKeyCode
import com.sun.jna.Native
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import java.io.File
import java.io.IOException

class FarsiVoiceRecognitionService(
    private val context: Context,
    private val onCommand: (RemoteKeyCode) -> Unit  // Change to domain RemoteKeyCode
) {
    companion object {
        init {
            try {
                // Ensure JNA initialization
                if (System.getProperty("jna.nosys") != "true") {
                    System.setProperty("jna.nosys", "true")
                }
                // Fix ambiguity by explicitly passing String parameter
                Native.register("jnidispatch")  // Changed from null to explicit library name
            } catch (e: Exception) {
                Log.e("VoskService", "Failed to initialize JNA", e)
            }
        }
    }
    private var speechService: SpeechService? = null
    private var model: Model? = null
    private var recognizer: Recognizer? = null
    private val recognizerScope = CoroutineScope(Dispatchers.IO)

    private var lastProcessedCommand: String = ""
    private var lastProcessedTime: Long = 0
    private val DEBOUNCE_TIME = 1500L // 1.5 second debounce

    private val _recognitionState = MutableStateFlow<RecognitionState>(RecognitionState.Idle)
    val recognitionState: StateFlow<RecognitionState> = _recognitionState

    sealed class RecognitionState {
        object Idle : RecognitionState()
        object Listening : RecognitionState()
        data class Result(val text: String) : RecognitionState()
        data class Error(val exception: Exception) : RecognitionState()
    }

    private val commandMappings = mapOf(
        "روشن" to RemoteKeyCode.KEYCODE_POWER,  // Update all these to use domain RemoteKeyCode
        "خاموش" to RemoteKeyCode.KEYCODE_POWER,
        "بالا" to RemoteKeyCode.KEYCODE_DPAD_UP,
        "پایین" to RemoteKeyCode.KEYCODE_DPAD_DOWN,
        "چپ" to RemoteKeyCode.KEYCODE_DPAD_LEFT,
        "راست" to RemoteKeyCode.KEYCODE_DPAD_RIGHT,
        "انتخاب" to RemoteKeyCode.KEYCODE_DPAD_CENTER,
        "تایید" to RemoteKeyCode.KEYCODE_DPAD_CENTER,
        "برگشت" to RemoteKeyCode.KEYCODE_BACK,
        "برگرد" to RemoteKeyCode.KEYCODE_BACK,
        "بیا عقب" to RemoteKeyCode.KEYCODE_BACK,
        "خانه" to RemoteKeyCode.KEYCODE_HOME,
        "خونه" to RemoteKeyCode.KEYCODE_HOME,
        "منو" to RemoteKeyCode.KEYCODE_MENU,
        "صدا بالا" to RemoteKeyCode.KEYCODE_VOLUME_UP,
        "صدا زیاد" to RemoteKeyCode.KEYCODE_VOLUME_UP,
        "صدا کم" to RemoteKeyCode.KEYCODE_VOLUME_DOWN,
        "صدا پایین" to RemoteKeyCode.KEYCODE_VOLUME_DOWN,
        "قطع صدا" to RemoteKeyCode.KEYCODE_MUTE,
        "صدا قطع" to RemoteKeyCode.KEYCODE_MUTE,
        "بی صدا" to RemoteKeyCode.KEYCODE_MUTE,
        "پخش" to RemoteKeyCode.KEYCODE_MEDIA_PLAY,
        "توقف" to RemoteKeyCode.KEYCODE_MEDIA_PAUSE,
        "بعدی" to RemoteKeyCode.KEYCODE_MEDIA_NEXT,
        "قبلی" to RemoteKeyCode.KEYCODE_MEDIA_PREVIOUS,
        "جلو" to RemoteKeyCode.KEYCODE_MEDIA_FAST_FORWARD,
        "عقب" to RemoteKeyCode.KEYCODE_MEDIA_REWIND,
        "تنظیمات" to RemoteKeyCode.KEYCODE_SETTINGS,
        "ورودی" to RemoteKeyCode.KEYCODE_TV_INPUT,
        "کانال بالا" to RemoteKeyCode.KEYCODE_CHANNEL_UP,
        "کانال بعدی" to RemoteKeyCode.KEYCODE_CHANNEL_UP,
        "کانال قبلی" to RemoteKeyCode.KEYCODE_CHANNEL_DOWN,
        "کانال پایین" to RemoteKeyCode.KEYCODE_CHANNEL_DOWN
    )

    private lateinit var recognitionListener: RecognitionListener

    private var isInitialized = false  // Add this flag

    init {
        recognitionListener = object : RecognitionListener {
            override fun onPartialResult(hypothesis: String?) {
                Log.d("VoskService", "Raw partial result received: $hypothesis")
                hypothesis?.let {
                    try {
                        val jsonResult = JSONObject(it)
                        val partialText = jsonResult.optString("partial")
                        Log.d("VoskService", "Parsed partial text: $partialText")
                        if (partialText.isNotEmpty()) {
                            _recognitionState.value = RecognitionState.Result(partialText)
                            processCommandWithDebounce(partialText, isPartial = true)
                        }
                    } catch (e: Exception) {
                        Log.e("VoskService", "Error processing partial result", e)
                        _recognitionState.value = RecognitionState.Error(e)
                    }
                }
            }

            override fun onResult(hypothesis: String?) {
                Log.d("VoskService", "Raw final result received: $hypothesis")
                hypothesis?.let {
                    try {
                        val jsonResult = JSONObject(it)
                        val text = jsonResult.optString("text")
                        Log.d("VoskService", "Parsed final text: $text")
                        if (text.isNotEmpty()) {
                            _recognitionState.value = RecognitionState.Result(text)
                            processCommandWithDebounce(text, isPartial = false)
                        }
                    } catch (e: Exception) {
                        Log.e("VoskService", "Error processing final result", e)
                        _recognitionState.value = RecognitionState.Error(e)
                    }
                }
            }

            override fun onFinalResult(hypothesis: String?) {
                Log.d("VoskService", "onFinalResult called with: $hypothesis")
                speechService?.let { service ->
                    Log.d("VoskService", "Restarting listening after final result")
                    service.startListening(recognitionListener)
                } ?: Log.e("VoskService", "Speech service is null in onFinalResult")
            }

            override fun onError(exception: Exception?) {
                Log.e("VoskService", "Recognition error", exception)
                exception?.let {
                    _recognitionState.value = RecognitionState.Error(it)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    Log.d("VoskService", "Attempting to restart after error")
                    speechService?.startListening(recognitionListener)
                }
            }

            override fun onTimeout() {
                Log.d("VoskService", "Recognition timeout - restarting")
                speechService?.startListening(recognitionListener)
            }
        }

        // Initialize Vosk (but don't start listening)
        // initializeVosk()
    }

    fun initializeVosk() {
        if (isInitialized) return

        recognizerScope.launch {
            try {
                Log.d("VoskService", "Starting Vosk initialization")

                val modelFolderName = "vosk-model-small-fa-0.42"
                val modelDestPath = File(context.cacheDir, modelFolderName)

                if (modelDestPath.exists()) {
                    modelDestPath.deleteRecursively()
                }
                modelDestPath.mkdirs()

                Log.d("VoskService", "Model destination path: ${modelDestPath.absolutePath}")
                copyAssetDirectory(modelFolderName, modelDestPath.absolutePath)

                Log.d("VoskService", "Initializing model...")
                model = Model(modelDestPath.absolutePath)
                Log.d("VoskService", "Model loaded successfully")

                recognizer = Recognizer(model, 16000.0f)
                Log.d("VoskService", "Recognizer created")

                recognizer?.let { rec ->
                    Log.d("VoskService", "Creating speech service...")
                    speechService = SpeechService(rec, 16000.0f)
                    // Don't auto-start listening here
                    _recognitionState.value = RecognitionState.Idle
                    isInitialized = true
                }
            } catch (e: Exception) {
                Log.e("VoskService", "Failed to initialize Vosk", e)
                _recognitionState.value = RecognitionState.Error(e)
                isInitialized = false
            }
        }
    }

    private fun processCommandWithDebounce(text: String, isPartial: Boolean) {
        val currentTime = System.currentTimeMillis()
        if (text == lastProcessedCommand && currentTime - lastProcessedTime < DEBOUNCE_TIME) {
            Log.d("VoskService", "Skipping duplicate command: $text")
            return
        }

        if (isPartial && currentTime - lastProcessedTime < DEBOUNCE_TIME) {
            return
        }

        Log.d("VoskService", "Processing ${if (isPartial) "partial" else "final"} command: $text")

        // First try exact phrase match
        if (commandMappings.containsKey(text)) {
            Log.d("VoskService", "Found exact command match: $text -> ${commandMappings[text]}")
            lastProcessedCommand = text
            lastProcessedTime = currentTime
            commandMappings[text]?.let { onCommand(it) }
            return
        }

        // Then try word combination matches
        val words = text.split(" ")
        // Try to match consecutive pairs of words first
        for (i in 0 until words.size - 1) {
            val twoWordPhrase = "${words[i]} ${words[i + 1]}"
            if (commandMappings.containsKey(twoWordPhrase)) {
                Log.d("VoskService", "Found two-word command match: $twoWordPhrase -> ${commandMappings[twoWordPhrase]}")
                lastProcessedCommand = text
                lastProcessedTime = currentTime
                commandMappings[twoWordPhrase]?.let { onCommand(it) }
                return
            }
        }

        // If no multi-word match, try individual words
        words.forEach { word ->
            if (commandMappings.containsKey(word)) {
                Log.d("VoskService", "Found single-word command match: $word -> ${commandMappings[word]}")
                lastProcessedCommand = text
                lastProcessedTime = currentTime
                commandMappings[word]?.let { onCommand(it) }
                return
            }
        }

        // Handle special case commands
        when {
            text.contains("صدا") && (text.contains("زیاد") || text.contains("بالا")) -> {
                Log.d("VoskService", "Found special case: volume up")
                lastProcessedCommand = text
                lastProcessedTime = currentTime
                onCommand(RemoteKeyCode.KEYCODE_VOLUME_UP)  // Using domain RemoteKeyCode
            }
            text.contains("صدا") && (text.contains("کم") || text.contains("پایین")) -> {
                Log.d("VoskService", "Found special case: volume down")
                lastProcessedCommand = text
                lastProcessedTime = currentTime
                onCommand(RemoteKeyCode.KEYCODE_VOLUME_DOWN)  // Using domain RemoteKeyCode
            }
            // Add more special cases as needed
        }

        Log.d("VoskService", "No matching command found for: $text")
    }

    private fun copyAssetDirectory(sourcePath: String, destinationPath: String) {
        try {
            val assets = context.assets.list(sourcePath)
            if (assets.isNullOrEmpty()) {
                copyAssetFile(sourcePath, destinationPath)
            } else {
                val destDir = File(destinationPath)
                if (!destDir.exists()) {
                    destDir.mkdirs()
                }

                for (asset in assets) {
                    val subSourcePath = if (sourcePath.isEmpty()) asset else "$sourcePath/$asset"
                    val subDestPath = "$destinationPath/$asset"

                    Log.d("VoskService", "Processing: $subSourcePath -> $subDestPath")

                    if (context.assets.list(subSourcePath)?.isNotEmpty() == true) {
                        copyAssetDirectory(subSourcePath, subDestPath)
                    } else {
                        copyAssetFile(subSourcePath, subDestPath)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("VoskService", "Error copying directory $sourcePath", e)
            throw e
        }
    }

    private fun copyAssetFile(sourcePath: String, destinationPath: String) {
        try {
            Log.d("VoskService", "Copying file: $sourcePath -> $destinationPath")
            context.assets.open(sourcePath).use { input ->
                File(destinationPath).outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: IOException) {
            Log.e("VoskService", "Failed to copy asset file: $sourcePath", e)
            throw e
        }
    }
    private fun initializeVoskAndStartListening() {
        recognizerScope.launch {
            try {
                val modelFolderName = "vosk-model-small-fa-0.42"
                val modelDestPath = File(context.cacheDir, modelFolderName)

                if (modelDestPath.exists()) {
                    modelDestPath.deleteRecursively()
                }
                modelDestPath.mkdirs()

                Log.d("VoskDebug", "Copying model files...")
                copyAssetDirectory(modelFolderName, modelDestPath.absolutePath)

                Log.d("VoskDebug", "Loading model...")
                model = Model(modelDestPath.absolutePath)
                recognizer = Recognizer(model, 16000.0f)
                speechService = SpeechService(recognizer, 16000.0f)

                Log.d("VoskDebug", "Model loaded and SpeechService created")
                isInitialized = true

                Log.d("VoskDebug", "Starting listening after model init...")
                speechService?.startListening(recognitionListener)
                _recognitionState.value = RecognitionState.Listening

            } catch (e: Exception) {
                Log.e("VoskDebug", "Failed to initialize and start listening", e)
                _recognitionState.value = RecognitionState.Error(e)
            }
        }
    }

    fun startListening() {
        if (!isInitialized) {
            Log.d("VoskDebug", "Model not initialized, initializing now...")
            initializeVoskAndStartListening()
        } else {
            Log.d("VoskDebug", "Model already initialized, starting listening...")
            speechService?.startListening(recognitionListener)
            _recognitionState.value = RecognitionState.Listening
        }
    }


    fun stopListening() {
        speechService?.stop()
        _recognitionState.value = RecognitionState.Idle
    }

    fun destroy() {
        stopListening()
        speechService = null

        recognizer?.let {
            try {
                it.close()
            } catch (e: Exception) {
                Log.e("VoskService", "Error closing recognizer", e)
            }
        }
        recognizer = null

        model?.let {
            try {
                it.close()
            } catch (e: Exception) {
                Log.e("VoskService", "Error closing model", e)
            }
        }
        model = null

        isInitialized = false  // Reset initialization flag
    }
}