package com.maadiran.myvision.presentation.features.fridge

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maadiran.myvision.presentation.features.main.MainScreenViewModel
import com.maadiran.myvision.presentation.ui.getBackgroundImageRes
import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel

@Composable
fun WifiForm(navController: NavController,
             themeViewModel: ThemeViewModel,
             viewModel: MainScreenViewModel = hiltViewModel()) {
    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // تصویر پس‌زمینه
        val currentTheme by themeViewModel.currentTheme.collectAsState()
        val backgroundImageRes = getBackgroundImageRes(currentTheme)

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = backgroundImageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // تصویر کل صفحه رو بگیره
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Wifi connection", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = ssid,
                onValueChange = { ssid = it },
                label = { Text("Network name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF0066FF), Color(0xFF184A94))
                        )
                    )
                    .clickable {
                        // پاک کردن کش قبل از ارسال اطلاعات
                        val prefs = context.getSharedPreferences("fridge_prefs", Context.MODE_PRIVATE)
                        prefs.edit().clear().apply()

                        val host = "192.168.4.1"
                        val port = 80
                        isLoading = true
                        sendWifiConfig(ssid, password, host, port, context, navController) {
                            isLoading = false
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Connect",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )

            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("serverStatus") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("دریافت از سرور")
            }



        }
    }

}