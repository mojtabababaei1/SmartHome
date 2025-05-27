package com.maadiran.myvision.presentation.features.devices.tv.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.ControlMode
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.QuickActionData
import com.maadiran.myvision.presentation.features.devices.tv.ui.theme.RemoteColors

@Composable
fun ImprovedQuickActionButton(
    data: QuickActionData,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .scale(scale)
                .shadow(
                    elevation = if (isPressed) 2.dp else 4.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .then(
                    if (data.gradient || data.isActive) {
                        Modifier.background(
                            brush = RemoteColors.primaryGradient,
                            shape = CircleShape
                        )
                    } else {
                        Modifier.background(
                            color = Color.White,
                            shape = CircleShape
                        )
                    }
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        isPressed = true
                        data.onClick()
                        isPressed = false
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.label,
                tint = if (data.gradient || data.isActive) Color.White else RemoteColors.PrimaryGradientEnd,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = data.label,
            style = MaterialTheme.typography.labelSmall,
            color = RemoteColors.DarkText,
            maxLines = 1
        )
    }
}

@Composable
fun ImprovedGridSection(
    title: String,
    items: List<QuickActionData>,
    columns: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = RemoteColors.DarkText,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(items.size) { index ->
                ImprovedQuickActionButton(items[index])
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprovedAlertCard(
    title: String,
    message: String,
    icon: ImageVector,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = RemoteColors.SecondaryColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(RemoteColors.PrimaryGradientEnd.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = RemoteColors.PrimaryGradientEnd,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = RemoteColors.DarkText
                    )
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = RemoteColors.MediumText
                    )
                }
            }

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Dismiss",
                    tint = RemoteColors.MediumText
                )
            }
        }
    }
}

@Composable
fun ImprovedProgressIndicator(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = RemoteColors.MediumText
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = RemoteColors.PrimaryGradientEnd,
                fontWeight = FontWeight.Medium
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = RemoteColors.PrimaryGradientEnd,
            trackColor = RemoteColors.PrimaryGradientEnd.copy(alpha = 0.2f),
        )
    }
}

@Composable
fun ImprovedDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = RemoteColors.SecondaryColor
    )
}

@Composable
fun ImprovedLoadingSpinner(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = RemoteColors.PrimaryGradientEnd,
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImprovedTopBar(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    selectedInput: String,
    onInputChange: (String) -> Unit
) {
    var showInputDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Smart TV Remote") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
            }
        },
        actions = {
            // Input Selection Button
            TextButton(
                onClick = { showInputDialog = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Icon(
                    Icons.Rounded.Input,
                    contentDescription = "Input",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(selectedInput)
            }

            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Filled.Settings, "Settings")
            }
        }
    )

    if (showInputDialog) {
        AlertDialog(
            onDismissRequest = { showInputDialog = false },
            title = { Text("Select Input") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("HDMI 1", "HDMI 2", "HDMI 3", "HDMI 4").forEach { input ->
                        Surface(
                            onClick = {
                                onInputChange(input)
                                showInputDialog = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = if (input == selectedInput)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        ) {
                            Text(
                                text = input,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showInputDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ImprovedTVStatusHeader(
    selectedInput: String,
    onInputChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Living Room TV", style = MaterialTheme.typography.titleMedium)
            ComboBox(
                selectedInput = selectedInput,
                options = listOf("HDMI 1", "HDMI 2", "HDMI 3", "HDMI 4"),
                onSelectionChange = onInputChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComboBox(
    selectedInput: String,
    options: List<String>,
    onSelectionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedInput,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectionChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MediaButton(
    icon: ImageVector,
    contentDescription: String,
    isPrimary: Boolean = false,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(if (isPrimary) 56.dp else 48.dp)
            .clip(CircleShape)
            .background(
                color = if (isPrimary) MaterialTheme.colorScheme.primary else Color.White,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isPrimary) Color.White else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(if (isPrimary) 32.dp else 24.dp)
        )
    }
}

@Composable
fun ImprovedKeyboardLayout(
    onKeyPress: (String) -> Unit,
    maxWidth: Dp,
    isPortrait: Boolean
) {
    val keys = listOf(
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M", "BACKSPACE")
    )

    Column(
        modifier = Modifier.width(maxWidth),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    KeyboardKey(key = key, onClick = { onKeyPress(key) })
                }
            }
        }

        // Space and Enter row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onKeyPress("SPACE") }) {
                Text("Space")
            }
            Button(onClick = { onKeyPress("ENTER") }) {
                Text("Enter")
            }
        }
    }
}

@Composable
private fun KeyboardKey(key: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (key == "BACKSPACE") {
                Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = key)
            } else {
                Text(key)
            }
        }
    }
}


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
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (mode) {
                    ControlMode.DPAD -> Icons.Filled.Gamepad
                    ControlMode.TOUCHPAD -> Icons.Filled.TouchApp
                    ControlMode.KEYPAD -> Icons.Filled.Dialpad
                    ControlMode.KEYBOARD -> Icons.Filled.Keyboard
                },
                contentDescription = null,
                tint = if (selected) Color.White else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = mode.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
@Composable
fun ImprovedVolumeControls(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = modifier.width(72.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onMuteToggle) {
                Icon(
                    imageVector = if (isMuted)
                        Icons.AutoMirrored.Rounded.VolumeOff
                    else
                        Icons.AutoMirrored.Rounded.VolumeUp,
                    contentDescription = if (isMuted) "Unmute" else "Mute"
                )
            }

            // Custom Vertical Slider implementation
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .width(48.dp)
            ) {
                val interactionSource = remember { MutableInteractionSource() }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { change, dragAmount ->
                                val pixelRange = size.height
                                val newPosition = dragAmount / pixelRange
                                onVolumeChange(((volume - newPosition * 100).coerceIn(0f, 100f)))
                            }
                        }
                ) {
                    // Draw track
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Draw active track
                    val activeTrackEnd = size.height * (1 - volume / 100)
                    drawLine(
                        color = colorScheme.primary,
                        start = Offset(size.width / 2, size.height),
                        end = Offset(size.width / 2, activeTrackEnd),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Draw thumb
                    drawCircle(
                        color = colorScheme.primary,
                        radius = 8.dp.toPx(),
                        center = Offset(size.width / 2, activeTrackEnd)
                    )
                }
            }

            Text(
                text = "${volume.toInt()}%",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeControlDialog(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight(),
        content = {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Volume Icon and Percentage
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onMuteToggle,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isMuted)
                                Icons.AutoMirrored.Rounded.VolumeOff
                            else
                                Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = if (isMuted) "Unmute" else "Mute",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "${volume.toInt()}%",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Large Volume Slider
                Slider(
                    value = volume,
                    onValueChange = onVolumeChange,
                    valueRange = 0f..100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                // Quick Volume Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    VolumePresetButton(label = "0%", onClick = { onVolumeChange(0f) })
                    VolumePresetButton(label = "25%", onClick = { onVolumeChange(25f) })
                    VolumePresetButton(label = "50%", onClick = { onVolumeChange(50f) })
                    VolumePresetButton(label = "75%", onClick = { onVolumeChange(75f) })
                    VolumePresetButton(label = "100%", onClick = { onVolumeChange(100f) })
                }
            }
        }
    )
}

@Composable
private fun VolumePresetButton(
    label: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.height(40.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}
@Composable
fun ImprovedAppShortcuts(
    shortcuts: List<AppShortcut>,
    isPortrait: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Quick Apps",
            style = MaterialTheme.typography.titleMedium
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(shortcuts.size) { index ->
                AppShortcutItem(shortcuts[index])
            }
        }
    }
}