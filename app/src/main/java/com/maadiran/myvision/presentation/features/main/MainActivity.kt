package com.maadiran.myvision.presentation.features.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.maadiran.myvision.core.platform.PermissionHandler
import com.maadiran.myvision.data.devices.tv.voice.FarsiVoiceRecognitionService
import com.maadiran.myvision.data.services.VoiceServiceManager
import com.maadiran.myvision.domain.model.RemoteKeyCode
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel
import com.maadiran.myvision.presentation.ui.MyVisionApp
import com.maadiran.myvision.presentation.ui.theme.MyVisionTheme
import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var voiceRecognitionService: FarsiVoiceRecognitionService? = null
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var voiceServiceManager: VoiceServiceManager

    private val mainViewModel: MainViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels() // ✅ اضافه کردن ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")

        permissionHandler = PermissionHandler(this)
        voiceServiceManager = VoiceServiceManager(this)

        // اجازه‌ها
        permissionHandler.checkAndRequestPermissions {
            // می‌توانی اینجا بعد از گرفتن اجازه، voice recognition را راه‌اندازی کنی
            initializeVoiceRecognition()
        }


        setContent {
            val theme by themeViewModel.currentTheme.collectAsState()

            MyVisionTheme(themeType = theme) {
                MyVisionApp(mainViewModel, voiceServiceManager)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHandler.handlePermissionResult(
            requestCode,
            permissions,
            grantResults
        ) {
            initializeVoiceRecognition()
        }
    }

    private fun initializeVoiceRecognition() {
        Log.d("VoskService", "Initializing voice recognition service")
        voiceRecognitionService = FarsiVoiceRecognitionService(
            context = this,
            onCommand = { keyCode: RemoteKeyCode ->
                Log.d("VoskService", "Command received in MainActivity: $keyCode")
                mainViewModel.sendKey(keyCode)
            }
        )
    }
}
