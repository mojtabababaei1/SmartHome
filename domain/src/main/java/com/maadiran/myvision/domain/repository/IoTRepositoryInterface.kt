package com.maadiran.myvision.domain.repository
import com.maadiran.myvision.domain.models.DoorStatus  // Updated import
import com.maadiran.myvision.models.TemperatureData

interface IoTRepositoryInterface {
    suspend fun configureWifi(deviceType: String, ssid: String, password: String): Result<String>
    suspend fun sendCommand(deviceType: String, command: String): Result<Boolean>
    suspend fun getTemperatures(deviceType: String): Result<TemperatureData>
    suspend fun getDoorStatuses(deviceType: String): Result<DoorStatus>
}