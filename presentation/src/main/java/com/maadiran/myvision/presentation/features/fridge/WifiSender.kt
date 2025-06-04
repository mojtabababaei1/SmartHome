package com.maadiran.myvision.presentation.features.fridge
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

fun sendWifiConfig(
    ssid: String,
    password: String,
    host: String,
    port: Int,
    context: Context,
    navController: NavController,
    onComplete: () -> Unit
) {
    if (ssid.isEmpty() || password.isEmpty()) return

    val client = OkHttpClient()
    val url = "http://$host:$port/connectTest"
    val json = """{"ssid":"$ssid", "password":"$password"}"""
    val body = json.toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder().url(url).post(body).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("ESP", "Failed: ${e.message}")
            onComplete()
        }

        override fun onResponse(call: Call, response: Response) {
            CoroutineScope(Dispatchers.IO).launch {
                var reachable = false

                // تا 40 ثانیه صبر کن و هر 4 ثانیه تست بزن
                for (i in 1..30) {
                    delay(1000)
                    Log.d("ESP", "Attempt ${i}: pinging fridge...")
                    if (pingFridge("refrigerator.local", 80)) {
                        reachable = true
                        break
                    }
                }

                withContext(Dispatchers.Main) {
                    if (reachable) {
                        saveFridgeAddress(context, "refrigerator.local", 80)
                        navController.navigate("fridge")
                    } else {
                        Log.e("ESP", "Fridge not reachable after retries.")
                        Toast.makeText(context, "اتصال به یخچال ممکن نشد", Toast.LENGTH_LONG).show()
                    }
                    onComplete()
                }
            }

        }



    })
}

fun pingFridge(host: String, port: Int): Boolean {
    return try {
        val socket = Socket()
        socket.connect(InetSocketAddress(host, port), 2000)
        socket.close()
        true
    } catch (e: IOException) {
        false
    }
}

fun saveFridgeAddress(context: Context, host: String, port: Int) {
    val prefs = context.getSharedPreferences("fridge_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("host", host).putInt("port", port).apply()
}
