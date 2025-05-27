package com.maadiran.myvision.presentation.ui.theme



import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xFF00796B)
val SecondaryColor = Color(0xFF4DB6AC)
val BackgroundColor = Color(0xFFF1F1F1)
val SurfaceColor = Color.White
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val ErrorColor = Color(0xFFD32F2F)

val myColorScheme = androidx.compose.material3.lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorColor,
    onError = Color.White,
)
