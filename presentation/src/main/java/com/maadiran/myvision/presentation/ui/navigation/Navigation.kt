package com.maadiran.myvision.presentation.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.presentation.features.devices.tv.ui.screens.GoogleTVPairingPage
import com.maadiran.myvision.presentation.features.devices.tv.ui.screens.SmartTvController
import com.maadiran.myvision.presentation.features.devices.tv.ui.screens.voice.GoogleVoicePage
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel
import com.maadiran.myvision.presentation.features.devices.washingmachine.ui.screens.WashingMachineScreen
import com.maadiran.myvision.presentation.features.fridge.FridgeNavGraph
import com.maadiran.myvision.presentation.features.fridge.SettingsScreen
import com.maadiran.myvision.presentation.features.main.SmartHubScreen
import com.maadiran.myvision.presentation.features.settings.MyVisionSettingsScreen
import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    voiceServiceManager: IVoiceServiceManager,

) {
    val isPaired by mainViewModel.isPaired.collectAsState()
    val connectionState by mainViewModel.connectionState.collectAsState()
    val context = LocalContext.current

    // Initialize voice service manager once
    LaunchedEffect(Unit) {
        mainViewModel.initializeVoiceManager(context)
    }
    val themeViewModel: ThemeViewModel = hiltViewModel()

    NavHost(navController, startDestination = "main") {
        composable("main") {
            SmartHubScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        // مسیر مخصوص یخچال با ناوبری داخلی خودش
        composable("fridgeModule") {
            FridgeNavGraph(navController = navController,
                themeViewModel = themeViewModel    )
        }

        composable("devices") {
            SmartHubScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }

        composable("settings") {
            SettingsScreen(navController = navController) 
        }
        composable("settingsTheme") {
            MyVisionSettingsScreen(
                navController = navController,
                themeViewModel = themeViewModel
            )
        }

        composable("tv-pairing") {
            LaunchedEffect(connectionState) {
                if (connectionState == MainViewModel.ConnectionState.Connected) {
                    navController.navigate("tv-control") {
                        popUpTo("main")
                    }
                }
            }
            GoogleTVPairingPage(
                onPairClick = { mainViewModel.startPairing() },
                onCodeEntered = { code -> mainViewModel.sendPairingCode(code) },
                isPairingStarted = connectionState is MainViewModel.ConnectionState.Connecting,
                onSsdpDiscovery = { mainViewModel.startSsdpDiscovery() },
                discoveredDevices = mainViewModel.discoveredDevices,
                onDeviceSelected = { deviceInfo ->
                    mainViewModel.setDeviceInfo(deviceInfo.host, null, null)
                    mainViewModel.startPairing()
                },
                isPairedPreviously = isPaired,
                onGoToController = {
                    mainViewModel.startPairing()
                    mainViewModel.initializeAndroidRemote()
                },
                connectionState = connectionState,
                discoveryError = mainViewModel.discoveryError.value
            )
        }

        composable("tv-control") {
            mainViewModel.getVoiceServiceManager()?.let { voiceServiceManager ->
                SmartTvController(
                    viewModel = mainViewModel,
                    voiceServiceManager = voiceServiceManager
                )
            } ?: Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Error: Voice service not initialized",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        composable(
            "wm-control/{ip}",
            arguments = listOf(navArgument("ip") { type = NavType.StringType })
        ) { backStackEntry ->
            val ip = backStackEntry.arguments?.getString("ip") ?: ""
            WashingMachineScreen(
                ip = ip,
                navController = navController
            )
        }

        composable("google-voice") {
            GoogleVoicePage(
                voiceServiceManager = voiceServiceManager,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
