package com.maadiran.myvision.presentation.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maadiran.myvision.domain.repository.IoTRepositoryInterface
import com.maadiran.myvision.domain.repository.PreferencesManagerInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: IoTRepositoryInterface,
    private val preferencesManager: PreferencesManagerInterface
) : ViewModel() {

    private val _deviceStatuses = MutableStateFlow<Map<String, DeviceStatus>>(emptyMap())
    val deviceStatuses = _deviceStatuses.asStateFlow()

    init {
        viewModelScope.launch {
            checkAllDevices()
        }
    }

    private suspend fun checkAllDevices() {
        val statuses = mutableMapOf<String, DeviceStatus>()

        // Check refrigerator


        // Check washing machine
        preferencesManager.getIpAddress("washing-machine")?.let { ip ->
            repository.sendCommand("washing-machine", "status")
                .onSuccess {
                    statuses["washing-machine"] = DeviceStatus.Connected
                }
                .onFailure {
                    statuses["washing-machine"] = DeviceStatus.Disconnected
                }
        }

        _deviceStatuses.update { statuses }
    }


}