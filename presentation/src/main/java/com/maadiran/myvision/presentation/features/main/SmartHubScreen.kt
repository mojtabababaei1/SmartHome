package com.maadiran.myvision.presentation.features.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel
import com.maadiran.myvision.presentation.features.settings.MyVisionSettingsScreen
import com.maadiran.myvision.presentation.ui.navigation.PlaceholderScreen
import com.maadiran.myvision.presentation.ui.navigation.SmartHubNavigationBar
import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel

@Composable
fun SmartHubScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val themeViewModel: ThemeViewModel = hiltViewModel()

    val isPaired by mainViewModel.isPaired.collectAsState()
    val selectedItem = remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            SmartHubNavigationBar(
                selectedItem = selectedItem,
                onHomeClick = {
                    selectedItem.value = 0
                },
                onVoiceClick = {
                    selectedItem.value = 1
                },
                onShoppingClick = {
                    selectedItem.value = 2
                },
                onTroubleshootingClick = {
                    selectedItem.value = 3
                },
                onProfileClick = {
                    selectedItem.value = 4
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem.value) {
                0 -> MainScreen(
                    navController = navController,
                    onTVClick = {
                        if (isPaired) {
                            navController.navigate("tv-control")
                        } else {
                            navController.navigate("tv-pairing")
                        }
                    },
                    modifier = modifier,
                    themeViewModel = themeViewModel
                )
                1 -> PlaceholderScreen("Voice")
                2 -> PlaceholderScreen("Shopping")
                3 -> PlaceholderScreen("Troubleshooting")
                4 -> MyVisionSettingsScreen(
                    navController = navController,
                    themeViewModel = themeViewModel
                )

            }
        }
    }
}


