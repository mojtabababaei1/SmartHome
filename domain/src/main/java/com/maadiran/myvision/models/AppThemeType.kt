package com.maadiran.myvision.models



enum class AppThemeType {
    Real,
    Fantasy;

    companion object {
        fun fromString(value: String?): AppThemeType {
            return when (value) {
                "Real" -> Real
                "Fantasy" -> Fantasy
                else -> Real
            }
        }
    }
}
