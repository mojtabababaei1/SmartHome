package com.maadiran.myvision.presentation.features.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maadiran.myvision.models.AppThemeType
import com.maadiran.myvision.presentation.settings.ThemeSelectorScreen
import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel

@Composable
fun MyVisionSettingsScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    modifier: Modifier = Modifier
) {
    val currentTheme by themeViewModel.currentTheme.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "تنظیمات برنامه",
            style = MaterialTheme.typography.headlineSmall
        )
        ThemeSelectorScreen(themeViewModel = themeViewModel)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val newTheme = if (currentTheme == AppThemeType.Real) AppThemeType.Fantasy else AppThemeType.Real
            themeViewModel.setTheme(newTheme)
        }) {
            Text("تغییر تم به ${if (currentTheme == AppThemeType.Real) "فانتزی" else "واقعی"}")
        }

    }
}
