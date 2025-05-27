package com.maadiran.myvision

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.AppShortcutsSection
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.ControlMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OriginalGoogleTVRemoteScreen() {
    var selectedInput by remember { mutableStateOf("HDMI 1") }
    var volume by remember { mutableStateOf(50f) }
    var isMuted by remember { mutableStateOf(false) }
    var controlMode by remember { mutableStateOf(ControlMode.DPAD) }
    var isVoiceControlActive by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RectangleShape)
    ) {
        // Top App Bar
        EnhancedTopBar(
            onBackClick = { /* Handle back */ },
            onSettingsClick = { /* Handle settings */ }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // TV Status
            TVStatusHeader(selectedInput)

            // Quick Actions
            QuickActionsRow(
                isMuted = isMuted,
                onMuteToggle = { isMuted = !isMuted },
                isVoiceControlActive = isVoiceControlActive,
                onVoiceControlToggle = { isVoiceControlActive = !isVoiceControlActive }
            )

            // Control Mode Selector
            ControlModeSelector(
                selectedMode = controlMode,
                onModeSelect = { controlMode = it }
            )

            // Main Control Area
            AnimatedContent(
                targetState = controlMode,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300))
                        .togetherWith(fadeOut(animationSpec = tween(300)))
                }
            ) { mode ->
                when (mode) {
                    ControlMode.DPAD -> DPadController()
                    ControlMode.TOUCHPAD -> TouchpadController()
                    ControlMode.KEYPAD -> KeypadController()
                    ControlMode.KEYBOARD -> KeyboardController()
                }
            }

            // Enhanced Channel Controls
            EnhancedChannelControls(
                volume = volume,
                onVolumeChange = { volume = it }
            )

            // Media Controls
            MediaControlsSection()

            // App Shortcuts
            AppShortcutsSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Voice Control overlay if active
    if (isVoiceControlActive) {
        VoiceControlOverlay(
            onDismiss = { isVoiceControlActive = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTopBar(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "TV Remote",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Badge {
                    Text("Connected")
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = DarkText,
            navigationIconContentColor = DarkText,
            actionIconContentColor = DarkText
        )
    )
}

@Composable
fun ControlModeSelector(
    selectedMode: ControlMode,
    onModeSelect: (ControlMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ControlMode.values().forEach { mode ->
            val selected = mode == selectedMode
            FilterChip(
                selected = selected,
                onClick = { onModeSelect(mode) },
                label = {
                    Text(
                        text = mode.name.lowercase().capitalize(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = when (mode) {
                            ControlMode.DPAD -> Icons.Rounded.Gamepad
                            ControlMode.TOUCHPAD -> Icons.Rounded.TouchApp
                            ControlMode.KEYPAD -> Icons.Rounded.Dialpad
                            ControlMode.KEYBOARD -> Icons.Rounded.Keyboard
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryGradientEnd,
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}