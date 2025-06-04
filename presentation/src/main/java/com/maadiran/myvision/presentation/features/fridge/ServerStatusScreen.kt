package com.maadiran.myvision.presentation.features.fridge




import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun ServerStatusScreen(navController: NavController) {
    var createdAt by remember { mutableStateOf("--") }
    var fridgeTemp by remember { mutableStateOf("--") }
    var freezerTemp by remember { mutableStateOf("--") }
    var defrostTemp by remember { mutableStateOf("--") }
    var fanStatus by remember { mutableStateOf("--") }
    var doorFridge by remember { mutableStateOf("--") }
    var doorFreezer by remember { mutableStateOf("--") }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("🕒 زمان: $createdAt")
        Text("🌡 دمای یخچال: $fridgeTemp °C")
        Text("🌡 دمای فریزر: $freezerTemp °C")
        Text("🌡 دمای دیفراست: $defrostTemp °C")
        Text("💨 وضعیت فن: $fanStatus")
        Text("🚪 درب یخچال: $doorFridge")
        Text("🚪 درب فریزر: $doorFreezer")

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        URL("http://78.39.129.10/api/monitoringtest/48:e7:29:78:c6:c5")
                            .openConnection().run {
                                connectTimeout = 3000
                                readTimeout = 3000
                                this as HttpURLConnection
                                requestMethod = "GET"
                                inputStream.bufferedReader().readText()
                            }
                    }

                    val json = JSONObject(response)

                    if (!json.has("created_at") || json.getString("created_at").isNullOrEmpty()) {
                        // داده معتبر نیست، ناوبری به صفحه تنظیمات
                        navController.navigate("settings")
                        return@launch
                    }

                    createdAt = json.getString("created_at")
                    fridgeTemp = json.getString("temperature_fridge")
                    freezerTemp = json.getString("temperature_freezer")
                    defrostTemp = json.getString("temperature_defrost")
                    fanStatus = json.getString("fan_status")
                    doorFridge = json.getString("door_status_fridge")
                    doorFreezer = json.getString("door_status_freezer")

                } catch (e: Exception) {
                    e.printStackTrace()
                    // در صورت خطا ناوبری به صفحه تنظیمات
                    navController.navigate("settings")
                }
            }
        }) {
            Text("بارگذاری اطلاعات از سرور")
        }
    }
}

