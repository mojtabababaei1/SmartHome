package com.maadiran.myvision.core.utils

object StringUtils {
    fun getDoorStatusInFarsi(status: String): String = when (status.lowercase()) {
        "open" -> "باز"
        "close" -> "بسته"
        else -> "نامشخص"
    }
}