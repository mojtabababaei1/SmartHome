package com.maadiran.myvision.presentation.features.fridge


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maadiran.myvision.presentation.features.settings.SettingsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

@Composable
fun FridgeNavGraph(navController: NavHostController) {
    val fridgeNavController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }

    // بررسی اتصال یخچال به صورت async
    LaunchedEffect(Unit) {
        val connected = withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress("refrigerator.local", 80), 2000)
                socket.close()
                true
            } catch (e: Exception) {
                false
            }
        }
        startDestination = if (connected) "fridge" else "WifiForm"
    }

    if (startDestination != null) {
        NavHost(
            navController = fridgeNavController,
            startDestination = startDestination!!
        ) {
            composable("WifiForm") {
                WifiForm(navController = fridgeNavController)
           }
            composable("fridge") {
                FridgeScreen()
            }
            composable("status") {
                Text("یخچال با موفقیت متصل شد!")
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    } else {
        // نمایش لودینگ یا هر UI دیگر در زمان بررسی اتصال
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
