package com.maadiran.myvision.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.maadiran.myvision.models.AppThemeType
import com.maadiran.myvision.presentation.ui.theme.FantasyColorScheme
import com.maadiran.myvision.presentation.ui.theme.RealColorScheme
import com.maadiran.myvision.presentation.ui.theme.Shapes
import com.maadiran.myvision.presentation.ui.theme.Typography

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
