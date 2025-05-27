package com.maadiran.myvision.domain.repository

interface PreferencesManagerInterface {
    fun saveIpAddress(deviceType: String, ip: String)
    fun getIpAddress(deviceType: String): String?
    fun clearIpAddress(deviceType: String)
}
