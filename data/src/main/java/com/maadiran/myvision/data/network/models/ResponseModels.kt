package com.maadiran.myvision.data.network.models

import com.google.gson.annotations.SerializedName

data class CommandResult(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String? = null
)

data class TemperatureData(
    @SerializedName("status")
    val status: String,
    @SerializedName("FridgeTemp")
    val FridgeTemp: Float,
    @SerializedName("FreezeTemp")
    val FreezeTemp: Float
)

data class DoorStatusResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("FridgeDoor")  // Matches API response field exactly
    val fridgeDoor: String,
    @SerializedName("FreezeDoor")   // Matches API response field exactly
    val freezerDoor: String
)

data class WifiConfig(
    @SerializedName("ssid")
    val ssid: String,
    @SerializedName("password")
    val password: String
)

data class WifiConnectResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("ssid")
    val ssid: String?,
    @SerializedName("ip")
    val ip: String?
)