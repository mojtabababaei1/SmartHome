package com.maadiran.myvision.presentation.ui



import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.maadiran.myvision.data.services.VoiceServiceManager
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel
import com.maadiran.myvision.presentation.ui.navigation.AppNavigation

@Composable
fun MyVisionApp(
    mainViewModel: MainViewModel,
    voiceServiceManager: VoiceServiceManager
) {
    val navController = rememberNavController()

    AppNavigation(
        navController = navController,
        mainViewModel = mainViewModel,
        voiceServiceManager = voiceServiceManager
    )
}
