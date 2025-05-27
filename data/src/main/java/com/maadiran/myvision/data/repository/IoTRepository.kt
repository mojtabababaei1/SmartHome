package com.maadiran.myvision.data.repository

import android.util.Log
import com.maadiran.myvision.data.network.DynamicBaseUrlInterceptor
import com.maadiran.myvision.data.network.IoTApiService
import com.maadiran.myvision.data.network.models.DoorStatusResponse
import com.maadiran.myvision.domain.models.DoorStatus
import com.maadiran.myvision.domain.repository.IoTRepositoryInterface
import com.maadiran.myvision.models.TemperatureData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IoTRepository @Inject constructor(
    private val apiService: IoTApiService,
    private val dynamicBaseUrlInterceptor: DynamicBaseUrlInterceptor
) : IoTRepositoryInterface {

    private val TAG = "IoT_DEBUG"

    init {
        // در ابتدای ساخت، baseURL را روی refrigerator.local تنظیم کن
        updateBaseUrl("refrigerator.local")
    }

    private fun updateBaseUrl(url: String) {
        Log.d(TAG, "Updating base URL to: $url")
        dynamicBaseUrlInterceptor.updateBaseUrl(url)
    }

    override suspend fun configureWifi(deviceType: String, ssid: String, password: String): Result<String> = runCatching {
        Log.d(TAG, "Sending WiFi configuration to $deviceType with SSID: $ssid")
        val response = apiService.configureWifi(ssid, password)

        if (response.isSuccessful) {
            val ip = response.body()?.ip ?: throw Exception("No IP received")
            Log.d(TAG, "Configured WiFi, device IP: $ip")
            ip
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e(TAG, "WiFi config failed: $errorBody")
            throw Exception("Failed to configure WiFi: $errorBody")
        }
    }.onFailure {
        Log.e(TAG, "WiFi configuration failed", it)
    }

    override suspend fun sendCommand(deviceType: String, command: String): Result<Boolean> = runCatching {
        Log.d(TAG, "Sending command '$command' to device: $deviceType")
        val response = apiService.sendCommand(command)

        if (!response.isSuccessful) {
            throw Exception("Command failed with code: ${response.code()}")
        }

        true
    }

    override suspend fun getTemperatures(deviceType: String): Result<TemperatureData> = runCatching {
        Log.d(TAG, "Getting temperature data from $deviceType")
        val response = apiService.getTemperatures()

        if (!response.isSuccessful) {
            throw Exception("Failed to get temperatures")
        }

        response.body()?.let {
            TemperatureData(
                fridgeTemp = it.FridgeTemp,
                freezerTemp = it.FreezeTemp
            )
        } ?: throw Exception("No temperature data received")
    }

    override suspend fun getDoorStatuses(deviceType: String): Result<DoorStatus> = runCatching {
        Log.d(TAG, "Getting door statuses from $deviceType")
        val response = apiService.getDoorStatuses()

        if (!response.isSuccessful) {
            throw Exception("Failed to get door statuses")
        }

        response.body()?.toDomainModel() ?: throw Exception("No door status data received")
    }

    private fun DoorStatusResponse.toDomainModel() = DoorStatus(
        fridgeDoor = fridgeDoor,
        freezerDoor = freezerDoor
    )
}
