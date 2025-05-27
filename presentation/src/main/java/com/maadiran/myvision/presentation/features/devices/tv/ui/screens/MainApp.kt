package com.maadiran.myvision.presentation.features.devices.tv.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel
import com.maadiran.myvision.presentation.features.voicecontrol.VoiceRecognitionPage
import com.maadiran.myvision.domain.utils.KeyCodeMapper

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainApp(
    mainViewModel: MainViewModel,
    voiceServiceManager: IVoiceServiceManager // Inject this from parent
) {
    val context = LocalContext.current
    var isPairingStarted by remember { mutableStateOf(false) }
    val isPaired by mainViewModel.isPaired.collectAsState()

    // Initialize voice service manager
    LaunchedEffect(Unit) {
        voiceServiceManager.initialize(
            onVoiceCommand = { command ->
                mainViewModel.sendKey(KeyCodeMapper.fromInt(command))
            },
            onUrlReceived = { url ->
                mainViewModel.sendAppLink(url)
            }
        )
    }

    // Check if permission is granted
    val hasAudioPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    // Navigation state
    var currentPage by remember { mutableStateOf("control") }

    // Set the callback to show the pairing code input
    mainViewModel.onPairingCodeRequested = {
        isPairingStarted = true
    }

    // Handle cleanup of voice service
    DisposableEffect(Unit) {
        onDispose {
            voiceServiceManager.destroy()
        }
    }

    if (!isPaired) {
        GoogleTVPairingPage(
            onPairClick = {
                Log.d("MainApp", "Pairing started")
                isPairingStarted = true
                mainViewModel.startPairing()
            },
            onCodeEntered = { code ->
                Log.d("MainApp", "Pairing code entered: $code")
                if (code.isNotEmpty()) {
                    mainViewModel.sendPairingCode(code)
                }
                isPairingStarted = false
            },
            isPairingStarted = isPairingStarted,
            onSsdpDiscovery = {
                mainViewModel.startSsdpDiscovery()
                Log.d("MainApp", "SSDP Discovery started")
            },
            discoveredDevices = mainViewModel.discoveredDevices,
            onDeviceSelected = { deviceInfo ->
                mainViewModel.setDeviceInfo(deviceInfo.host, null, null)
                isPairingStarted = true
                mainViewModel.startPairing()
            },
            onGoToController = {
                mainViewModel.startPairing()
                mainViewModel.initializeAndroidRemote()
            },
            isPairedPreviously = isPaired,
            connectionState = mainViewModel.connectionState.value,
            discoveryError = mainViewModel.discoveryError.value
        )
    } else {
        when (currentPage) {
            "control" -> {
                SmartTvController(
                    viewModel = mainViewModel,
                    voiceServiceManager = voiceServiceManager
                )
            }
            "voice" -> {
                VoiceRecognitionPage(
                    viewModel = mainViewModel,
                    onBackClick = {
                        currentPage = "control"
                        Log.d("MainApp", "Back to ControlPage")
                    }
                )
            }
        }
    }

}