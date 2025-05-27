package com.maadiran.myvision.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class GoogleSpeechRecognizer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    class GoogleSpeechContract : ActivityResultContract<Unit, Result<String>>() {
        override fun createIntent(context: Context, input: Unit): Intent =
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fa")
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "لطفاً صحبت کنید")
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Result<String> {
            if (resultCode != Activity.RESULT_OK) {
                return Result.failure(Exception("Speech recognition failed"))
            }

            val results = intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            return if (results.isNullOrEmpty()) {
                Result.failure(Exception("No speech detected"))
            } else {
                Result.success(results[0])
            }
        }
    }

    suspend fun startGoogleSpeechRecognition(activity: Activity): Result<String> =
        suspendCancellableCoroutine { continuation ->
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa")
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                }

                activity.startActivityForResult(
                    intent,
                    SPEECH_REQUEST_CODE
                )

            } catch (e: Exception) {
                continuation.resume(Result.failure(e))
            }
        }

    companion object {
        const val SPEECH_REQUEST_CODE = 123
    }
}