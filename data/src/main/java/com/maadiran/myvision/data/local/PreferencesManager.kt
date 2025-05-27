package com.maadiran.myvision.data.local

import android.content.Context
import android.content.SharedPreferences
import com.maadiran.myvision.domain.repository.PreferencesManagerInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val context: Context
) : PreferencesManagerInterface {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "smart_home_prefs",
        Context.MODE_PRIVATE
    )

    override fun saveIpAddress(deviceType: String, ip: String) {
        prefs.edit().putString("${KEY_IP_ADDRESS}_$deviceType", ip).apply()
    }

    override fun getIpAddress(deviceType: String): String? =
        prefs.getString("${KEY_IP_ADDRESS}_$deviceType", null)

    override fun clearIpAddress(deviceType: String) {
        prefs.edit().remove("${KEY_IP_ADDRESS}_$deviceType").apply()
    }

    companion object {
        private const val KEY_IP_ADDRESS = "ip_address"
    }
}