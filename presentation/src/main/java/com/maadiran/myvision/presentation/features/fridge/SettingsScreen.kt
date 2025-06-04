package com.maadiran.myvision.presentation.features.fridge


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

@Composable
fun SettingsScreen(navController: NavController) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Settings Page", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                navController.navigate("WifiForm")
            }) {
                Text("WifiForm")
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    val isConnected = checkFridgeConnection()
                    if (isConnected) {
                        navController.navigate("fridge")
                    } else {
                        navController.navigate("settings") // یا همون صفحه فعلی بمونه
                    }
                }
            }) {
                Text("بررسی اتصال مجدد")
            }
        }
    }
}

suspend fun checkFridgeConnection(): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress("refrigmb.local", 80), 2000)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}

