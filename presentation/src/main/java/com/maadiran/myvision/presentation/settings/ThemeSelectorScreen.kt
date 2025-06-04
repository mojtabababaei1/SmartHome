package com.maadiran.myvision.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.maadiran.myvision.models.AppThemeType

import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel

@Composable
fun ThemeSelectorScreen(themeViewModel: ThemeViewModel) {
    val currentTheme by themeViewModel.currentTheme.collectAsState() //

    Column {
        Text("Select Theme:")
        Row {
            Button(
                onClick = { themeViewModel.setTheme(AppThemeType.Real) },
                enabled = currentTheme != AppThemeType.Real
            ) {
                Text("Real")
            }
            Button(
                onClick = { themeViewModel.setTheme(AppThemeType.Fantasy) },
                enabled = currentTheme != AppThemeType.Fantasy
            ) {
                Text("Fantasy")
            }
        }
    }
}
