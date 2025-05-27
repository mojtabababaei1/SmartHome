package com.maadiran.myvision.presentation.features.devices.washingmachine.ui.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.ui.graphics.vector.ImageVector
import com.maadiran.myvision.presentation.R

data class WMScreenState(
    val isLoading: Boolean = false,
    val commandStatuses: Map<WMCommand, Boolean> = emptyMap(),
    val error: String? = null,
    val isListening: Boolean = false,
    val recognizedText: String = ""
)

enum class WMCommand(
    val key: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int
) {
    POWER("power", Icons.Default.Power, R.string.power),
    START_STOP("startstop", Icons.Default.PlayArrow, R.string.start_stop),
    TEMPERATURE("temp", Icons.Default.Thermostat, R.string.temperature),
    SPEED("speed", Icons.Default.Speed, R.string.speed),
    MODE("select", Icons.Default.SelectAll, R.string.mode)
}