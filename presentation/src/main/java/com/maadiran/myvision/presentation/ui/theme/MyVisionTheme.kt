package com.maadiran.myvision.presentation.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyVisionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = myColorScheme,
        typography = myTypography,
        content = content
    )
}
