package com.maadiran.myvision.presentation.features.devices.tv.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object RemoteColors {
    val PrimaryGradientStart = Color(0xFFB7A8F8)
    val PrimaryGradientEnd = Color(0xFF7052F2)
    val SecondaryColor = Color(0xFFDADAFD)
    val AccentColor = Color(0xFF7052F2).copy(alpha = 0.1f)

    val DarkText = Color.Black.copy(alpha = 0.8f)
    val MediumText = Color.Black.copy(alpha = 0.6f)
    val LightText = Color.Black.copy(alpha = 0.4f)

    val SuccessColor = Color(0xFF4CAF50)
    val WarningColor = Color(0xFFFFC107)
    val ErrorColor = Color(0xFFF44336)
    val InfoColor = Color(0xFF2196F3)

    val primaryGradient = Brush.linearGradient(
        colors = listOf(PrimaryGradientStart, PrimaryGradientEnd)
    )

    val secondaryGradient = Brush.linearGradient(
        colors = listOf(SecondaryColor, Color.White)
    )

    val buttonPressed = Color.Black.copy(alpha = 0.1f)
    val buttonDisabled = Color.Black.copy(alpha = 0.12f)
}