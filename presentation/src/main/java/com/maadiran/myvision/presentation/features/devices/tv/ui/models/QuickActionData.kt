package com.maadiran.myvision.presentation.features.devices.tv.ui.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a quick action button in the TV remote
 * @param icon The vector icon to display
 * @param label The text label for the action
 * @param gradient Whether to use gradient background
 * @param isActive Whether the action is in active state
 * @param onClick Callback for when the action is clicked
 */


data class QuickActionData(
    val icon: ImageVector,
    val label: String,
    val gradient: Boolean = false,
    val isActive: Boolean = false,
    val onClick: () -> Unit = {}
) {
    companion object {
        fun defaults() = listOf(
            QuickActionData(
                icon = Icons.Rounded.PowerSettingsNew,
                label = "Power",
                gradient = true
            ),
            QuickActionData(
                icon = Icons.AutoMirrored.Rounded.VolumeUp,
                label = "Volume"
            ),
            QuickActionData(
                icon = Icons.Rounded.Home,
                label = "Home"
            ),
            QuickActionData(
                icon = Icons.Rounded.Mic,
                label = "Voice"
            ),
            QuickActionData(
                icon = Icons.Rounded.ScreenRotation,
                label = "Rotate"
            ),
            QuickActionData(
                icon = Icons.Rounded.Settings,
                label = "Settings"
            )
        )
    }
}