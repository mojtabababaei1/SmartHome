package com.maadiran.myvision.presentation.features.fridge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun FridgeTemperatureMonitor() {

    var fridgeTemp by remember { mutableStateOf("--") }
    var freezeTemp by remember { mutableStateOf("--") }

    val fridgeTempHistory = remember { mutableStateListOf<Float>() }
    val freezeTempHistory = remember { mutableStateListOf<Float>() }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                withContext(Dispatchers.IO) {
                    val tempsJson = URL("http://refrigerator.local/api/MonitorTemps")
                        .openConnection().run {
                            connectTimeout = 3000
                            readTimeout = 3000
                            this as HttpURLConnection
                            requestMethod = "GET"
                            inputStream.bufferedReader().readText()
                        }

                    withContext(Dispatchers.Main) {
                        fridgeTemp = extractValue(tempsJson, "FridgeTemp")
                        freezeTemp = extractValue(tempsJson, "FreezeTemp")

                        val fridgeFloat = fridgeTemp.toFloatOrNull()
                        val freezeFloat = freezeTemp.toFloatOrNull()

                        if (fridgeFloat != null) {
                            if (fridgeTempHistory.size >= 20) fridgeTempHistory.removeAt(0)
                            fridgeTempHistory.add(fridgeFloat)
                        }

                        if (freezeFloat != null) {
                            if (freezeTempHistory.size >= 20) freezeTempHistory.removeAt(0)
                            freezeTempHistory.add(freezeFloat)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CombinedTemperatureChart(
            fridgeTemps = fridgeTempHistory,
            freezeTemps = freezeTempHistory,
            modifier = Modifier
                .size(width = 300.dp, height = 100.dp)
        )
    }

}
