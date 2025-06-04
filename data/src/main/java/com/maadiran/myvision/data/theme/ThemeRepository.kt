package com.maadiran.myvision.data.theme

import com.maadiran.myvision.data.theme.datastore.ThemePreferenceManager
import com.maadiran.myvision.models.AppThemeType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepository @Inject constructor(
    private val themePreferenceManager: ThemePreferenceManager
) {

    val themeFlow: Flow<AppThemeType> = themePreferenceManager.themeFlow

    suspend fun setTheme(theme: AppThemeType) {
        themePreferenceManager.setTheme(theme)
    }
}


