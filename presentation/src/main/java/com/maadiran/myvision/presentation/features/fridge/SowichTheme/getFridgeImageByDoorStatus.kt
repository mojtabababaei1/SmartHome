package com.maadiran.myvision.presentation.features.fridge.SowichTheme



import com.maadiran.myvision.models.AppThemeType
import com.maadiran.myvision.presentation.R

fun getFridgeImageByDoorStatus(
    isFridgeOpen: Boolean,
    isFreezerOpen: Boolean,
    theme: AppThemeType
): Int {
    return when {
        isFridgeOpen && !isFreezerOpen -> {
            when (theme) {
                AppThemeType.Fantasy -> R.drawable.fantasy_up_fridge_door_open
                AppThemeType.Real -> R.drawable.real_up_fridge_door_open
            }
        }
        !isFridgeOpen && isFreezerOpen -> {
            when (theme) {
                AppThemeType.Fantasy -> R.drawable.fantasy_down_freezer_door_open
                AppThemeType.Real -> R.drawable.real_down_freezer_door_open
            }
        }
        isFridgeOpen && isFreezerOpen -> {
            when (theme) {
                AppThemeType.Fantasy -> R.drawable.fantasy_both_open
                AppThemeType.Real -> R.drawable.real_both_open
            }
        }
        else -> {
            when (theme) {
                AppThemeType.Fantasy -> R.drawable.fantasy_all_closed
                AppThemeType.Real -> R.drawable.real_all_closed
            }
        }
    }
 }
//تابع فن
fun getFanImageRes(theme: AppThemeType): Int {
    return when (theme) {
        AppThemeType.Fantasy -> R.drawable.fantasy_fan
        AppThemeType.Real -> R.drawable.real_fan
    }
}