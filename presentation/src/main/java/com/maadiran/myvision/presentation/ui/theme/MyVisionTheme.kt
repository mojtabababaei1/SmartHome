package com.maadiran.myvision.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.maadiran.myvision.models.AppThemeType

@Composable
fun MyVisionTheme(
    themeType: AppThemeType = AppThemeType.Real,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeType) {
        AppThemeType.Fantasy -> FantasyColorScheme
        AppThemeType.Real -> RealColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


