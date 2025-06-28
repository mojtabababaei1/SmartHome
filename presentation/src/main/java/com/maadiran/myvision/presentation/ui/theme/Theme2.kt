//package com.maadiran.myvision.presentation.ui.theme
//
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.darkColorScheme
//import androidx.compose.material3.lightColorScheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.runtime.staticCompositionLocalOf
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//
//// Original theme colors
//private val PrimaryGradientStart = Color(0xFFB7A8F8)
//private val PrimaryGradientEnd = Color(0xFF7052F2)
//private val SecondaryBackground = Color(0xFFDADAFD)
//
//// Extended color palette
//private val LightBackgroundStart = Color(0xFFF0F7FF)  // blue-50
//private val LightBackgroundEnd = Color(0xFFF5F3FF)    // purple-50
//
//data class ExtendedColors(
//    val cardBackground: Color = Color(0xFFFFFFFF),
//    val blueTint: Color = Color(0xFFDDEEFF),
//    val indigoTint: Color = Color(0xFFE0E7FF),
//    val greenTint: Color = Color(0xFFDCFCE7),
//    val textPrimary: Color = Color(0xFF1F2937),
//    val textSecondary: Color = Color(0xFF6B7280)
//)
//
//data class ExtendedGradients(
//    val backgroundGradient: Brush = Brush.verticalGradient(
//        colors = listOf(LightBackgroundStart, LightBackgroundEnd)
//    ),
//    val quickFreezeGradient: Brush = Brush.horizontalGradient(
//        colors = listOf(Color(0xFF9333EA), Color(0xFF3B82F6))
//    ),
//    val ecoModeGradient: Brush = Brush.horizontalGradient(
//        colors = listOf(Color(0xFF60A5FA), Color(0xFF06B6D4))
//    ),
//    val fridgeGradient: Brush = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF60A5FA),  // blue-400
//            Color(0xFF3B82F6)   // blue-500
//        )
//    ),
//    val freezerGradient: Brush = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF818CF8),  // indigo-400
//            Color(0xFF6366F1)   // indigo-500
//        )
//    ),
//    val warningGradient: Brush = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFFFCA5A5),  // red-300
//            Color(0xFFEF4444)   // red-500
//        )
//    ),
//    val successGradient: Brush = Brush.linearGradient(
//        colors = listOf(
//            Color(0xFF86EFAC),  // green-300
//            Color(0xFF22C55E)   // green-500
//        )
//    )
//)
//
//val LocalExtendedColors = staticCompositionLocalOf { ExtendedColors() }
//val LocalExtendedGradients = staticCompositionLocalOf { ExtendedGradients() }
//
//private val LightColors = lightColorScheme(
//    primary = PrimaryGradientEnd,
//    secondary = SecondaryBackground,
//    tertiary = PrimaryGradientStart,
//    surface = Color(0xFFFFFBFE),
//    background = Color(0xFFFFFBFE)
//)
//
//private val DarkColors = darkColorScheme(
//    primary = PrimaryGradientEnd,
//    secondary = SecondaryBackground,
//    tertiary = PrimaryGradientStart,
//    surface = Color(0xFF1C1B1F),
//    background = Color(0xFF1C1B1F)
//)
//
//@Composable
//fun MyVisionTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable () -> Unit
//) {
//    val colorScheme = if (darkTheme) DarkColors else LightColors
//    val extendedColors = ExtendedColors()
//    val extendedGradients = ExtendedGradients()
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        content = {
//            CompositionLocalProvider(
//                LocalExtendedColors provides extendedColors,
//                LocalExtendedGradients provides extendedGradients
//            ) {
//                content()
//            }
//        }
//    )
//}

// Convenience object to access extended theme
//object AppTheme {
//    val colors: ExtendedColors
//        @Composable
//        get() = LocalExtendedColors.current
//
//    val gradients: ExtendedGradients
//        @Composable
//        get() = LocalExtendedGradients.current
//}