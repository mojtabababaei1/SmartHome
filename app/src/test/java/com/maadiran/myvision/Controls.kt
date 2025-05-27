package com.maadiran.myvision

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults


@Composable
fun TouchpadController() {
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .size(280.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        touchPosition += dragAmount
                    }
                }
        ) {
            // Touch indicator
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .offset {
                        IntOffset(
                            touchPosition.x.toInt().coerceIn(0, 260),
                            touchPosition.y.toInt().coerceIn(0, 260)
                        )
                    }
                    .clip(CircleShape)
                    .background(color = PrimaryGradientEnd.copy(alpha = 0.5f), shape = CircleShape)
            )

            // OK Button
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(CircleShape)
                    .then(
                        Modifier.background(brush = primaryGradient, shape = CircleShape)
                    )
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "OK",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun DPadController() {
    Box(
        modifier = Modifier
            .size(260.dp)
            .clip(CircleShape)
            .background(color = SecondaryColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val center = Offset(maxWidth.value / 2f, maxHeight.value / 2f)
            val buttonRadius = 28.dp
            val distanceFromCenter = 70.dp

            // Center button (OK)
            Box(
                modifier = Modifier
                    .offset(
                        x = (center.x - 32.dp.value).dp,
                        y = (center.y - 32.dp.value).dp
                    )
            ) {
                DPadButton(
                    icon = Icons.Rounded.CheckCircle,
                    gradient = true,
                    size = 64.dp
                )
            }

            // Up button
            Box(
                modifier = Modifier
                    .offset(
                        x = (center.x - buttonRadius.value).dp,
                        y = (center.y - distanceFromCenter.value - buttonRadius.value).dp
                    )
            ) {
                DPadButton(
                    icon = Icons.Rounded.KeyboardArrowUp,
                    size = buttonRadius * 2
                )
            }

            // Left button
            Box(
                modifier = Modifier
                    .offset(
                        x = (center.x - distanceFromCenter.value - buttonRadius.value).dp,
                        y = (center.y - buttonRadius.value).dp
                    )
            ) {
                DPadButton(
                    icon = Icons.Rounded.KeyboardArrowLeft,
                    size = buttonRadius * 2
                )
            }

            // Right button
            Box(
                modifier = Modifier
                    .offset(
                        x = (center.x + distanceFromCenter.value - buttonRadius.value).dp,
                        y = (center.y - buttonRadius.value).dp
                    )
            ) {
                DPadButton(
                    icon = Icons.Rounded.KeyboardArrowRight,
                    size = buttonRadius * 2
                )
            }

            // Down button
            Box(
                modifier = Modifier
                    .offset(
                        x = (center.x - buttonRadius.value).dp,
                        y = (center.y + distanceFromCenter.value - buttonRadius.value).dp
                    )
            ) {
                DPadButton(
                    icon = Icons.Rounded.KeyboardArrowDown,
                    size = buttonRadius * 2
                )
            }
        }
    }
}

@Composable
private fun DPadButton(
    icon: ImageVector,
    gradient: Boolean = false,
    size: Dp = 56.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = if (gradient) 8.dp else 4.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .then(
                if (gradient) {
                    Modifier.background(brush = primaryGradient, shape = CircleShape)
                } else {
                    Modifier.background(color = Color.White, shape = CircleShape)
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (gradient) Color.White else PrimaryGradientEnd,
            modifier = Modifier.size(size / 2)
        )
    }
}






@Composable
fun KeypadController() {
    var displayValue by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .width(280.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Channel Number Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayValue.ifEmpty { "Enter Channel" },
                    style = MaterialTheme.typography.titleLarge,
                    color = if (displayValue.isEmpty()) LightText else DarkText,
                    fontWeight = FontWeight.Medium
                )
            }

            // Number Pad Grid
            val numbers = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("←", "0", "GO")
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                numbers.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { number ->
                            NumpadKey(
                                text = number,
                                isSpecial = number in listOf("←", "GO"),
                                onClick = {
                                    when (number) {
                                        "←" -> if (displayValue.isNotEmpty()) {
                                            displayValue = displayValue.dropLast(1)
                                        }
                                        "GO" -> {
                                            if (displayValue.isNotEmpty()) {
                                                // Handle channel change
                                            }
                                        }
                                        else -> if (displayValue.length < 4) {
                                            displayValue += number
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NumpadKey(
    text: String,
    isSpecial: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .then(
                if (isSpecial && text == "GO") {
                    Modifier.background(brush = primaryGradient, shape = CircleShape)
                } else {
                    Modifier.background(color = if (isSpecial) SecondaryColor else Color.White, shape = CircleShape)
                }
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        when (text) {
            "←" -> Icon(
                imageVector = Icons.Rounded.Backspace,
                contentDescription = "Backspace",
                tint = PrimaryGradientEnd,
                modifier = Modifier.size(24.dp)
            )
            else -> Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = when {
                    text == "GO" -> Color.White
                    isSpecial -> PrimaryGradientEnd
                    else -> DarkText
                },
                fontWeight = if (isSpecial) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}






@Composable
fun KeyboardController() {
    var searchQuery by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search TV shows, movies, apps...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = PrimaryGradientEnd
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear",
                                tint = MediumText
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGradientEnd,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            )

            // Virtual Keyboard
            VirtualKeyboard(
                onKeyPress = { key ->
                    when (key) {
                        "⌫" -> searchQuery = searchQuery.dropLast(1)
                        "Space" -> searchQuery += " "
                        "Enter" -> { /* Handle search */ }
                        else -> searchQuery += key
                    }
                }
            )
        }
    }
}

@Composable
fun VirtualKeyboard(onKeyPress: (String) -> Unit) {
    val rows = listOf(
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M", "⌫")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (row.size < 10) {
                    Spacer(modifier = Modifier.width((10 - row.size) * 16.dp))
                }

                row.forEach { key ->
                    KeyboardKey(
                        text = key,
                        onClick = { onKeyPress(key) }
                    )
                }
            }
        }

        // Space bar row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeyboardKey(
                text = "Space",
                onClick = { onKeyPress("Space") },
                modifier = Modifier.weight(1f)
            )
            KeyboardKey(
                text = "Enter",
                onClick = { onKeyPress("Enter") },
                isAccent = true,
                modifier = Modifier.width(100.dp)
            )
        }
    }
}

@Composable
fun KeyboardKey(
    text: String,
    onClick: () -> Unit,
    isAccent: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (isAccent) {
                    Modifier.background(brush = primaryGradient, shape = RoundedCornerShape(8.dp))
                } else {
                    Modifier.background(color = Color.White, shape = RoundedCornerShape(8.dp))
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isAccent) Color.White else DarkText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EnhancedChannelControls(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Volume Control
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SecondaryColor),
            shape = RoundedCornerShape(20.dp)
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
                        style = MaterialTheme.typography.titleSmall,
                        color = DarkText
                    )
                    Text(
                        text = "${(volume * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MediumText
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onVolumeChange((volume - 0.1f).coerceIn(0f, 1f)) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeDown,
                            contentDescription = "Decrease Volume",
                            tint = PrimaryGradientEnd
                        )
                    }

                    Slider(
                        value = volume,
                        onValueChange = onVolumeChange,
                        colors = SliderDefaults.colors(
                            thumbColor = PrimaryGradientEnd,
                            activeTrackColor = PrimaryGradientEnd,
                            inactiveTrackColor = PrimaryGradientEnd.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { onVolumeChange((volume + 0.1f).coerceIn(0f, 1f)) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.VolumeUp,
                            contentDescription = "Increase Volume",
                            tint = PrimaryGradientEnd
                        )
                    }
                }
            }
        }

        // Channel Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Channel Number Display
            Card(
                modifier = Modifier.weight(0.4f),
                colors = CardDefaults.cardColors(containerColor = SecondaryColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Channel",
                        style = MaterialTheme.typography.titleSmall,
                        color = DarkText
                    )
                    Text(
                        text = "12",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryGradientEnd,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Channel Controls
            Card(
                modifier = Modifier.weight(0.6f),
                colors = CardDefaults.cardColors(containerColor = SecondaryColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChannelButton(
                        icon = Icons.Rounded.KeyboardArrowDown,
                        label = "Previous"
                    )
                    VerticalDivider()
                    ChannelButton(
                        icon = Icons.Rounded.KeyboardArrowUp,
                        label = "Next"
                    )
                }
            }
        }
    }
}

@Composable
fun ChannelButton(
    icon: ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PrimaryGradientEnd,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MediumText
        )
    }
}



@Composable
fun VoiceControlOverlay(onDismiss: () -> Unit) {
    var isListening by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f), shape = RectangleShape)
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
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
                                Modifier.background(brush = primaryGradient, shape = CircleShape)
                            } else {
                                Modifier.background(color = SecondaryColor, shape = CircleShape)
                            }
                        )
                        .clickable { isListening = !isListening },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Mic,
                        contentDescription = "Microphone",
                        tint = if (isListening) Color.White else PrimaryGradientEnd,
                        modifier = Modifier.size(32.dp)
                    )
                }

                if (isListening) {
                    Text(
                        text = "Try saying:\n\"Change to channel 5\"\n\"Open Netflix\"\n\"Volume up\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MediumText,
                        textAlign = TextAlign.Center
                    )
                }

                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimaryGradientEnd)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}