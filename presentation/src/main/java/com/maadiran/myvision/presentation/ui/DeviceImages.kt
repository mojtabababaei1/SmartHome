package com.maadiran.myvision.presentation.ui


import com.maadiran.myvision.models.AppThemeType
import com.maadiran.myvision.presentation.R

fun getTVImageRes(theme: AppThemeType): Int {
    return when (theme) {
        AppThemeType.Fantasy -> R.drawable.smart_tv_fantasy
        AppThemeType.Real -> R.drawable.smart_tv_real
    }
}

fun getWasherImageRes(theme: AppThemeType): Int {
    return when (theme) {
        AppThemeType.Fantasy -> R.drawable.washer_fantasy
        AppThemeType.Real -> R.drawable.washer_real
    }
}

fun getRefrigeImageRes(theme: AppThemeType): Int {
    return when (theme) {
        AppThemeType.Fantasy -> R.drawable.smart_frige_fantasy
        AppThemeType.Real -> R.drawable.smart_fridge_real
    }
}

fun getAirCImageRes(theme: AppThemeType): Int {
    return when (theme) {
        AppThemeType.Fantasy -> R.drawable.smart_airconditioner_fantasy
        AppThemeType.Real -> R.drawable.smart_airconditioner_real
    }
}


