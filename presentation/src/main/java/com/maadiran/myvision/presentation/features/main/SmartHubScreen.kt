package com.maadiran.myvision.presentation.features.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel
import com.maadiran.myvision.presentation.ui.navigation.SmartHubNavigationBar

@Composable
fun SmartHubScreen(
    navController: NavController,
    mainViewModel: MainViewModel,  // Add mainViewModel parameter
    modifier: Modifier = Modifier
) {
    var currentSection by remember { mutableStateOf("home") }
    val isPaired by mainViewModel.isPaired.collectAsState()

    Scaffold(
        bottomBar = {
            SmartHubNavigationBar(
                onHomeClick = {
                    currentSection = "home"
                },
                onDevicesClick = {
                    currentSection = "devices"
                },
                onSettingsClick = {
                    currentSection = "settings"
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentSection) {
                "home" -> MainScreen(
                    navController = navController,
                    onTVClick = {
                        if (isPaired) {
                            navController.navigate("tv-control")
                        } else {
                            navController.navigate("tv-pairing")
                        }
                    },
                    modifier = modifier
                )
                "devices" -> DevicesScreen(
                    navController = navController,
                    modifier = modifier
                )
                "settings" -> SettingsScreen(
                    navController = navController,
                    modifier = modifier
                )
            }
        }
    }
}
@Composable
fun DevicesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Implement your devices screen here
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Devices",
            style = MaterialTheme.typography.headlineLarge
        )
        // Add your devices list or grid here
    }
}

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Implement your settings screen here
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge
        )
        // Add your settings options here
    }
}