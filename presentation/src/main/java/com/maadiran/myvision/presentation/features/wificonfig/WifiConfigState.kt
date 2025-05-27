package com.maadiran.myvision.presentation.features.wificonfig

data class WifiConfigState(
    val isLoading: Boolean = false,
    val ssid: String = "",
    val password: String = "",
    val ip: String = "",
    val error: String? = null,
    val isConfigured: Boolean = false
)