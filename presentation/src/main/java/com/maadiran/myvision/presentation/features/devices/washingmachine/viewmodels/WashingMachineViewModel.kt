package com.maadiran.myvision.presentation.features.devices.washingmachine.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maadiran.myvision.domain.repository.IoTRepositoryInterface
import com.maadiran.myvision.presentation.features.devices.washingmachine.ui.models.WMCommand
import com.maadiran.myvision.presentation.features.devices.washingmachine.ui.models.WMScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WashingMachineViewModel @Inject constructor(
    private val ioTRepository: IoTRepositoryInterface,  // Changed from IoTRepository
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "WM_DEBUG"
    // Define constant for device type to ensure consistency
    private val DEVICE_TYPE = "washing-machine"  // Match the WiFi configuration

    private val _state = MutableStateFlow(WMScreenState())
    val state = _state.asStateFlow()

    private var updateJob: Job? = null

    init {
        Log.d(TAG, "WashingMachineViewModel initialized")
        startPeriodicUpdates()
    }

    private fun startPeriodicUpdates() {
        Log.d(TAG, "Starting periodic updates")
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            while (isActive) {
                try {
                    Log.d(TAG, "Fetching washing machine status...")
                    // Add status fetching methods here similar to refrigerator
                    // Example: fetchWashingMachineStatus()
                    delay(1000) // Check every second
                } catch (e: Exception) {
                    Log.e(TAG, "Error in periodic update", e)
                }
            }
        }
    }

    fun sendCommand(command: WMCommand) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            ioTRepository.sendCommand(DEVICE_TYPE, command.key)  // Use constant
                .onSuccess { success ->
                    Log.d(TAG, "Command ${command.key} sent successfully: $success")
                    _state.update { currentState ->
                        currentState.copy(
                            commandStatuses = currentState.commandStatuses + (command to success),
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "Failed to send command ${command.key}", error)
                    _state.update { it.copy(error = error.message) }
                }

            _state.update { it.copy(isLoading = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared, cancelling jobs")
        updateJob?.cancel()
    }

    fun handleSpeechResult(text: String) {
        viewModelScope.launch {
            _state.update { it.copy(
                isListening = false,
                recognizedText = text
            )}
            processVoiceCommand(text)
        }
    }

    fun handleSpeechError(error: Throwable) {
        Log.e(TAG, "Speech recognition error", error)
        viewModelScope.launch {
            _state.update { it.copy(
                isListening = false,
                error = error.message
            )}
        }
    }

    private fun processVoiceCommand(text: String) {
        val command = when {
            text.contains("روشن") || text.contains("خاموش") ||
                    text.contains("power") -> WMCommand.POWER

            text.contains("شروع") || text.contains("توقف") ||
                    text.contains("start") || text.contains("stop") -> WMCommand.START_STOP

            text.contains("دما") || text.contains("temperature") ||
                    text.contains("temp") -> WMCommand.TEMPERATURE

            text.contains("سرعت") || text.contains("speed") -> WMCommand.SPEED

            text.contains("انتخاب") || text.contains("حالت") ||
                    text.contains("select") -> WMCommand.MODE

            else -> null
        }

        command?.let { sendCommand(it) }
    }
}