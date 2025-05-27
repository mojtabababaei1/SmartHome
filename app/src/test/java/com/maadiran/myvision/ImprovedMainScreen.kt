package com.maadiran.myvision


import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.AppShortcutItem
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedKeyboardLayout
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedQuickActionButton
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.appShortcuts
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.ControlMode
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.QuickActionData
import com.maadiran.myvision.presentation.features.devices.tv.ui.theme.RemoteColors

@Composable
fun ImprovedTVRemoteScreen() {
    var selectedInput by remember { mutableStateOf("HDMI 1") }
    var volume by remember { mutableStateOf(50f) }
    var isMuted by remember { mutableStateOf(false) }
    var controlMode by remember { mutableStateOf(ControlMode.DPAD) }
    var isVoiceControlActive by remember { mutableStateOf(false) }

    // Handle screen rotation
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Safe volume handling
    var safeVolume by remember {
        mutableStateOf(volume.coerceIn(0f, 100f))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = if (isPortrait) 16.dp else 24.dp)
                .systemBarsPadding()
        ) {
            // Improved Top Bar
            ImprovedTopBar(
                onBackClick = { /* Handle back */ },
                onSettingsClick = { /* Handle settings */ }
            )

            // Main Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isPortrait) 16.dp else 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // TV Status Section
                item {
                    ImprovedTVStatusHeader(selectedInput)
                }

                // Quick Actions Grid
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        ImprovedQuickActionsGrid(
                            isMuted = isMuted,
                            onMuteToggle = { isMuted = !isMuted },
                            isVoiceControlActive = isVoiceControlActive,
                            onVoiceControlToggle = { isVoiceControlActive = !isVoiceControlActive },
                            isPortrait = isPortrait
                        )
                    }
                }

                // Control Mode Selection
                item {
                    ImprovedControlModeSelector(
                        selectedMode = controlMode,
                        onModeSelect = { controlMode = it },
                        isPortrait = isPortrait
                    )
                }

                // Main Control Area
                item {
                    AnimatedContent(
                        targetState = controlMode,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                                    fadeOut(animationSpec = tween(300))
                        }, label = ""
                    ) { mode ->
                        ImprovedControlArea(
                            mode = mode,
                            isPortrait = isPortrait,
                            maxWidth = if (isPortrait) 360.dp else 480.dp
                        )
                    }
                }

                // Volume and Channel Controls
                item {
                    ImprovedVolumeControls(
                        volume = safeVolume,
                        onVolumeChange = { newVolume ->
                            safeVolume = newVolume.coerceIn(0f, 100f)
                        },
                        isMuted = isMuted,
                        onMuteToggle = { isMuted = !isMuted }
                    )
                }

                // Media Controls Section
                item {
                    ImprovedMediaControls(isPortrait)
                }

                // App Shortcuts
                item {

                    ImprovedAppShortcuts(isPortrait)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Voice Control Overlay
        AnimatedVisibility(
            visible = isVoiceControlActive,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            ImprovedVoiceControlOverlay(
                onDismiss = { isVoiceControlActive = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImprovedTopBar(
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
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = RemoteColors.PrimaryGradientEnd.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "Connected",
                        style = MaterialTheme.typography.labelSmall,
                        color = RemoteColors.PrimaryGradientEnd,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = RemoteColors.DarkText
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = RemoteColors.DarkText
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = RemoteColors.DarkText,
            navigationIconContentColor = RemoteColors.DarkText,
            actionIconContentColor = RemoteColors.DarkText
        ),
        modifier = Modifier.shadow(elevation = 0.dp)
    )
}





// Add these components to ImprovedMainScreen.kt

@Composable
fun ImprovedControlModeSelector(
    selectedMode: ControlMode,
    onModeSelect: (ControlMode) -> Unit,
    isPortrait: Boolean
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isPortrait) 8.dp else 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ControlMode.values().size) { index ->
            val mode = ControlMode.values()[index]
            ControlModeChip(
                mode = mode,
                selected = mode == selectedMode,
                onClick = { onModeSelect(mode) }
            )
        }
    }
}

@Composable
private fun ControlModeChip(
    mode: ControlMode,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        color = if (selected) RemoteColors.PrimaryGradientEnd else RemoteColors.SecondaryColor,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (mode) {
                    ControlMode.DPAD -> Icons.Rounded.Gamepad
                    ControlMode.TOUCHPAD -> Icons.Rounded.TouchApp
                    ControlMode.KEYPAD -> Icons.Rounded.Dialpad
                    ControlMode.KEYBOARD -> Icons.Rounded.Keyboard
                },
                contentDescription = null,
                tint = if (selected) Color.White else RemoteColors.PrimaryGradientEnd,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = mode.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) Color.White else RemoteColors.DarkText
            )
        }
    }
}
@Composable
fun ImprovedVolumeControls(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit
) {
    var currentVolume by remember { mutableStateOf(volume / 100f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = RemoteColors.SecondaryColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Volume",
                    style = MaterialTheme.typography.titleMedium,
                    color = RemoteColors.DarkText
                )
                Text(
                    text = "${(currentVolume * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RemoteColors.MediumText
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onMuteToggle) {
                    Icon(
                        imageVector = if (isMuted) Icons.AutoMirrored.Rounded.VolumeOff else Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = if (isMuted) "Unmute" else "Mute",
                        tint = RemoteColors.PrimaryGradientEnd
                    )
                }

                Slider(
                    value = currentVolume,
                    onValueChange = {
                        currentVolume = it
                        onVolumeChange(it * 100)
                    },
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = RemoteColors.PrimaryGradientEnd,
                        activeTrackColor = RemoteColors.PrimaryGradientEnd,
                        inactiveTrackColor = RemoteColors.PrimaryGradientEnd.copy(alpha = 0.2f)
                    )
                )
            }
        }
    }
}

@Composable
fun ImprovedMediaControls(isPortrait: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = RemoteColors.SecondaryColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaButton(Icons.Rounded.SkipPrevious, "Previous")
            MediaButton(Icons.Rounded.Replay10, "Rewind")
            MediaButton(Icons.Rounded.PlayArrow, "Play/Pause", true)
            MediaButton(Icons.Rounded.Forward10, "Forward")
            MediaButton(Icons.Rounded.SkipNext, "Next")
        }
    }
}


@Composable
fun ImprovedTVStatusHeader(selectedInput: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = RemoteColors.SecondaryColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Living Room TV",
                    style = MaterialTheme.typography.titleMedium,
                    color = RemoteColors.DarkText
                )
                Text(
                    text = "Input: $selectedInput",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RemoteColors.MediumText
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = RemoteColors.AccentColor)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Cast,
                    contentDescription = "Connected",
                    tint = RemoteColors.PrimaryGradientEnd,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
@Composable
fun ImprovedQuickActionsGrid(
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    isVoiceControlActive: Boolean,
    onVoiceControlToggle: () -> Unit,
    isPortrait: Boolean
) {
    val actionButtons = listOf(
        QuickActionData(
            icon = Icons.Rounded.Power,
            label = "Power",
            gradient = true
        ),
        QuickActionData(
            icon = if (isMuted) Icons.AutoMirrored.Rounded.VolumeOff else Icons.AutoMirrored.Rounded.VolumeUp,
            label = if (isMuted) "Unmute" else "Mute",
            onClick = onMuteToggle
        ),
        QuickActionData(
            icon = Icons.Rounded.Mic,
            label = "Voice",
            isActive = isVoiceControlActive,
            onClick = onVoiceControlToggle
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (isPortrait) 3 else 6),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(120.dp)  // Fixed height for grid
        ) {
            items(actionButtons.size) { index ->
                ImprovedQuickActionButton(actionButtons[index])
            }
        }
    }
}

@Composable
fun ImprovedControlArea(
    mode: ControlMode,
    isPortrait: Boolean,
    maxWidth: Dp
) {
    val controlAreaSize = if (isPortrait) {
        maxWidth.coerceAtMost(360.dp)
    } else {
        maxWidth.coerceAtMost(480.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(controlAreaSize)
            .padding(16.dp)
    ) {
        when (mode) {
            ControlMode.KEYBOARD -> ImprovedKeyboardLayout(
                maxWidth = controlAreaSize,
                isPortrait = isPortrait
            )
            else -> Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RemoteColors.SecondaryColor
                )
            ) {
                /*when (mode) {
                    ControlMode.DPAD -> ImprovedDPadLayout()
                    ControlMode.TOUCHPAD -> ImprovedTouchpadLayout()
                    ControlMode.KEYPAD -> ImprovedKeypadLayout()
                    else -> Unit
                }*/// No content for now//
            }
        }
    }
}


@Composable
private fun MediaButton(
    icon: ImageVector,
    contentDescription: String,
    isPrimary: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(if (isPrimary) 56.dp else 48.dp)
            .shadow(if (isPrimary) 8.dp else 4.dp, CircleShape)
            .clip(CircleShape)
            .then(
                if (isPrimary) {
                    Modifier.background(RemoteColors.primaryGradient)
                } else {
                    Modifier.background(Color.White)
                }
            )
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isPrimary) Color.White else RemoteColors.PrimaryGradientEnd,
            modifier = Modifier.size(if (isPrimary) 32.dp else 24.dp)
        )
    }
}

@Composable
fun ImprovedAppShortcuts(isPortrait: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Quick Apps",
            style = MaterialTheme.typography.titleMedium,
            color = RemoteColors.DarkText
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(appShortcuts.size) { index ->
                AppShortcutItem(appShortcuts[index])
            }
        }
    }
}

@Composable
fun ImprovedVoiceControlOverlay(onDismiss: () -> Unit) {
    var isListening by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isListening) "Listening..." else "Press to speak",
                    style = MaterialTheme.typography.titleMedium
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .then(
                            if (isListening) {
                                Modifier.background(brush = RemoteColors.primaryGradient)
                            } else {
                                Modifier.background(color = RemoteColors.SecondaryColor)
                            }
                        )
                        .clickable { isListening = !isListening },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Mic,
                        contentDescription = "Microphone",
                        tint = if (isListening) Color.White else RemoteColors.PrimaryGradientEnd,
                        modifier = Modifier.size(32.dp)
                    )
                }
                if (isListening) {
                    Text(
                        text = "Try saying:\n\"Change channel\"\n\"Open Netflix\"\n\"Volume up\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RemoteColors.MediumText,
                        textAlign = TextAlign.Center
                    )
                }

                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = RemoteColors.PrimaryGradientEnd
                    )
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}