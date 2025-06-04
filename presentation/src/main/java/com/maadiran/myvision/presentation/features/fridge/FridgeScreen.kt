package com.maadiran.myvision.presentation.features.fridge

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maadiran.myvision.presentation.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun FridgeScreen(    navController: NavController,
                     modifier: Modifier = Modifier
) {
    var fridgeDoor by remember { mutableStateOf("unknown") }
    var freezeDoor by remember { mutableStateOf("unknown") }
    var fridgeTemp by remember { mutableStateOf("--") }
    var freezeTemp by remember { mutableStateOf("--") }
    var fridgeFan by remember { mutableStateOf("unknown") }

    // اجرای خودکار هر 10 ثانیه
    LaunchedEffect(Unit) {
        while (true) {
            try {
                withContext(Dispatchers.IO) {
                    val doorsJson = URL("http://refrigmb.local/api/MonitorDoors")
                        .openConnection().run {
                            connectTimeout = 3000
                            readTimeout = 3000
                            this as HttpURLConnection
                            requestMethod = "GET"
                            inputStream.bufferedReader().readText()
                        }

                    val tempsJson = URL("http://refrigmb.local/api/MonitorTemps")
                        .openConnection().run {
                            connectTimeout = 3000
                            readTimeout = 3000
                            this as HttpURLConnection
                            requestMethod = "GET"
                            inputStream.bufferedReader().readText()
                        }

                    val fanJson = URL("http://refrigmb.local/api/MonitorFan")
                        .openConnection().run {
                            connectTimeout = 3000
                            readTimeout = 3000
                            this as HttpURLConnection
                            requestMethod = "GET"
                            inputStream.bufferedReader().readText()
                        }

                    withContext(Dispatchers.Main) {
                        fridgeDoor = extractValue(doorsJson, "FridgeDoor")
                        freezeDoor = extractValue(doorsJson, "FreezeDoor")
                        fridgeTemp = extractValue(tempsJson, "FridgeTemp")
                        freezeTemp = extractValue(tempsJson, "FreezeTemp")
                        fridgeFan = extractValue(fanJson, "Fan")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            delay(5_000)
        }
    }

    val isFridgeOpen = fridgeDoor.equals("open", ignoreCase = true)
    val isFreezerOpen = freezeDoor.equals("open", ignoreCase = true)

    val fridgeImageRes = when {
        isFridgeOpen && !isFreezerOpen -> R.drawable.up_fridge_door_open
        !isFridgeOpen && isFreezerOpen -> R.drawable.doewn_freezer_door_open
        isFridgeOpen && isFreezerOpen -> R.drawable.both_open
        else -> R.drawable.all_closed
    }


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        // تصویر پس‌زمینه
        Image(
            painter = painterResource(id = R.drawable.fridge_background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // تصویر یخچال (بسته به وضعیت درب‌ها)
        Image(
            painter = painterResource(id = fridgeImageRes),
            contentDescription = "Fridge Image",
            modifier = Modifier
                .align(Alignment.TopCenter) // قرارگیری در بالا و مرکز
                .offset(y = 120.dp) // پایین‌تر آوردن تصویر به اندازه 40dp
                .fillMaxWidth(0.7f)
                .aspectRatio(1f) // نسبت تصویر (می‌تونی تغییر بدی)
                .height(400.dp)  // ارتفاع تصویر
        )
        Image(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Settings",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(32.dp)
                .clickable {
                    navController.navigate("settings")
                    Log.d("FridgeScreen", "Settings icon clicked")
                }
        )
        // آیکن فن (فقط اگر فن روشن است)
        if (fridgeFan.contains("on", ignoreCase = true)) {
            val infiniteTransition = rememberInfiniteTransition()
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = LinearEasing)
                )
            )

            Image(
                painter = painterResource(id = R.drawable.fan_icon),
                contentDescription = "Fan",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomCenter)
                    .offset(x = 0.dp, y = -150.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
            )
        }


        val fridgeTempInt = fridgeTemp.toIntOrNull() ?: 0
        val freezeTempInt = freezeTemp.toIntOrNull() ?: 0

        val fridgeIcon = when {
            fridgeTempInt < 0 -> R.drawable.ic_cold
            fridgeTempInt in 0..10 -> R.drawable.ic_normal_ice
            else -> R.drawable.ic_hot
        }

        val freezerIcon = when {
            freezeTempInt < -10 -> R.drawable.ic_super_ice
            freezeTempInt in -10..0 -> R.drawable.ic_cold
            else -> R.drawable.ic_warning
        }
// دمای یخچال
        // دمای یخچال
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 200.dp)
                .offset(x = 0.dp, y = -150.dp)

        ) {
            Image(
                painter = painterResource(id = fridgeIcon),
                contentDescription = "Fridge Temperature",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Fridge: $fridgeTemp°C",
                fontSize = 16.sp,
                color = Color.Black // در صورت نیاز به رنگ متن
            )
        }

// دمای فریزر
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(end = 200.dp)

                .offset(x = 0.dp, y = -150.dp)
        ) {
            Image(
                painter = painterResource(id = freezerIcon),
                contentDescription = "Freezer Temperature",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Freezer: $freezeTemp°C",
                fontSize = 16.sp,
                color = Color.Black // در صورت نیاز به رنگ متن
            )
        }


        // وضعیت درب‌ها پایین صفحه
        Text(
            text = "Fridge Door: $fridgeDoor | Freezer Door: $freezeDoor",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )

        //قسمت چارت (صدا زده شده است)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp)
                .size(width = 320.dp, height = 160.dp)
        ) {
            FridgeTemperatureMonitor()

        }

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