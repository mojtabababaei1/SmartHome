package com.maadiran.myvision.presentation.features.wificonfig

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maadiran.myvision.domain.repository.IoTRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiConfigViewModel @Inject constructor(
    private val ioTRepository: IoTRepositoryInterface,  // Changed from IoTRepository
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceType: String = savedStateHandle.get<String>("deviceType") ?: ""
    private val _state = MutableStateFlow(WifiConfigState())
    val state = _state.asStateFlow()

    fun updateSSID(ssid: String) {
        _state.update { it.copy(ssid = ssid) }
    }

    fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun configureWifi() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            ioTRepository.configureWifi(deviceType, state.value.ssid, state.value.password)
                .onSuccess { ip ->
                    _state.update { it.copy(
                        ip = ip,
                        error = null,
                        isConfigured = true
                    )}
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message) }
                }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun getNavigationRoute(): String = when (deviceType) {
        "washing-machine" -> "wm-control/${state.value.ip}"
        "refrigerator" -> "rfg-control/${state.value.ip}"
        else -> "main"
    }
}