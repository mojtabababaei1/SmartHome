package com.maadiran.myvision.data.theme.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.maadiran.myvision.models.AppThemeType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

@Singleton
class ThemePreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {

        private val THEME_KEY = stringPreferencesKey("app_theme")
    }


    val themeFlow: Flow<AppThemeType> = context.dataStore.data
        .map { preferences: Preferences ->
            val themeName = preferences[THEME_KEY]
            AppThemeType.fromString(themeName)
        }


    suspend fun setTheme(theme: AppThemeType) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }
}
