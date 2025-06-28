package com.maadiran.myvision.presentation.features.fridge


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maadiran.myvision.models.AppThemeType
import com.maadiran.myvision.presentation.features.fridge.SowichTheme.FantasyFridgeScreen
import com.maadiran.myvision.presentation.features.fridge.SowichTheme.RealFridgeScreen
import com.maadiran.myvision.presentation.features.fridge.SowichTheme.getFanImageRes
import com.maadiran.myvision.presentation.features.fridge.SowichTheme.getFridgeImageByDoorStatus
import com.maadiran.myvision.presentation.features.main.MainScreenViewModel
import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun FridgeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {

        var fridgeDoor by remember { mutableStateOf("unknown") }
        var freezeDoor by remember { mutableStateOf("unknown") }
        var fridgeTemp by remember { mutableStateOf("--") }
        var freezeTemp by remember { mutableStateOf("--") }
        var fridgeFan by remember { mutableStateOf("unknown") }
        val isFridgeOpen = fridgeDoor.equals("open", ignoreCase = true)
        val isFreezerOpen = freezeDoor.equals("open", ignoreCase = true)
        val currentTheme by themeViewModel.currentTheme.collectAsState()
    val fridgeImageRes = getFridgeImageByDoorStatus(isFridgeOpen, isFreezerOpen, currentTheme)

    // اجرای هر ۵ ثانیه برای دریافت اطلاعات از API
    LaunchedEffect(Unit) {
        while (true) {
            try {
                withContext(Dispatchers.IO) {
                    val doorsJson = URL("http://refrigerator.local/api/MonitorDoors").run {
                        openConnection().apply {
                            connectTimeout = 3000
                            readTimeout = 3000
                            this as HttpURLConnection
                            requestMethod = "GET"
                        }.inputStream.bufferedReader().readText()
                    }

                    val tempsJson = URL("http://refrigerator.local/api/MonitorTemps").run {
                        openConnection().apply {
                            connectTimeout = 3000
                            readTimeout = 3000
                            this as HttpURLConnection
                            requestMethod = "GET"
                        }.inputStream.bufferedReader().readText()
                    }

                    val fanJson = URL("http://refrigerator.local/api/MonitorFan").run {
                        openConnection().apply {
                            connectTimeout = 3000
                            readTimeout = 3000
                            this as HttpURLConnection
                            requestMethod = "GET"
                        }.inputStream.bufferedReader().readText()
                    }

                    withContext(Dispatchers.Main) {
                        fridgeDoor = extractValue(doorsJson, "FridgeDoor0")
                        freezeDoor = extractValue(doorsJson, "FreezeDoor0")
                        fridgeTemp = extractValue(tempsJson, "FridgeTemp")
                        freezeTemp = extractValue(tempsJson, "FreezeTemp")
                        fridgeFan = extractValue(fanJson, "Fan")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(5000)
        }
    }
    val fanImageRes = getFanImageRes(currentTheme)
    when (currentTheme) {
        AppThemeType.Real -> RealFridgeScreen(
            modifier,
            fridgeDoor,
            freezeDoor,
            fridgeTemp,
            freezeTemp,
            fridgeFan,
            fridgeImageRes,
            fanImageRes,
            navController
        )

        AppThemeType.Fantasy -> FantasyFridgeScreen(
            modifier,
            fridgeDoor,
            freezeDoor,
            fridgeTemp,
            freezeTemp,
            fridgeFan,
            fridgeImageRes,
            fanImageRes,
            navController
        )
    }
}

//         تابع JSON از نوع regex
// تابع ساده برای خواندن مقدار از JSON به صورت دستی
//fun extractValue(json: String, key: String): String {
//  val regex = """"$key"\s*:\s*"?(.*?)"?(,|\})""".toRegex()
//   return regex.find(json)?.groups?.get(1)?.value ?: "--"
//}

//                     همان تابع JSON بالا است ولی تو در تو نیست
fun extractValue(json: String, key: String): String {
    return try {
        val jsonObject = JSONObject(json)
        if (jsonObject.has(key)) jsonObject.get(key).toString()
        else "--"
    } catch (e: Exception) {
        "--"
    }
}
