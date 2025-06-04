package com.maadiran.myvision.presentation.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maadiran.myvision.data.theme.datastore.ThemePreferenceManager
import com.maadiran.myvision.models.AppThemeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferenceManager: ThemePreferenceManager
) : ViewModel() {

    private val _currentTheme = MutableStateFlow(AppThemeType.Real)
    val currentTheme: StateFlow<AppThemeType> = _currentTheme.asStateFlow()

    init {
        viewModelScope.launch {
            themePreferenceManager.themeFlow.collect { theme ->
                _currentTheme.value = theme
            }
        }
    }

    fun setTheme(theme: AppThemeType) {
        viewModelScope.launch {
            themePreferenceManager.setTheme(theme)
        }
    }
}
