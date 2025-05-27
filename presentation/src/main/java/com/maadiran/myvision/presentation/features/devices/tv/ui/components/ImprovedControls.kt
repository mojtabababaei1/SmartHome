package com.maadiran.myvision.presentation.features.devices.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.*
import com.maadiran.myvision.domain.model.RemoteKeyCode
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.QuickActionData
import com.maadiran.myvision.presentation.features.devices.tv.ui.theme.RemoteColors
import kotlin.math.abs

@Composable
fun DPadButton(
    icon: ImageVector,
    onClick: () -> Unit = {},
    isCenter: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(if (isCenter) 72.dp else 56.dp)
            .shadow(
                elevation = if (isCenter) 8.dp else 4.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .then(
                if (isCenter) {
                    Modifier.background(brush = RemoteColors.primaryGradient)
                } else {
                    Modifier.background(color = Color.White)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isCenter) Color.White else RemoteColors.PrimaryGradientEnd,
            modifier = Modifier.size(if (isCenter) 36.dp else 28.dp)
        )
    }
}

@Composable
fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .then(
                if (isPrimary) {
                    Modifier.background(brush = RemoteColors.primaryGradient)
                } else {
                    Modifier.background(Color.White)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = if (isPrimary) Color.White else RemoteColors.DarkText
        )
    }
}


@Composable
fun ImprovedQuickActionsGrid(
    actions: List<QuickActionData>,
    isPortrait: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Grid(
            columns = if (isPortrait) 2 else 3,
            modifier = Modifier.fillMaxWidth()
        ) {
            actions.forEach { action ->
                ImprovedQuickActionButton(action)
            }
        }
    }
}


@Composable
fun Grid(
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val itemWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth
        )

        val placeables = measurables.map { it.measure(itemConstraints) }
        val rows = (placeables.size + columns - 1) / columns
        val height = rows * placeables.firstOrNull()?.height.orZero()


        layout(constraints.maxWidth, height) {
            var x = 0
            var y = 0
            placeables.forEach { placeable ->
                placeable.place(x = x, y = y)
                x += itemWidth
                if (x >= constraints.maxWidth) {
                    x = 0
                    y += placeable.height
                }
            }
        }
    }
}

private fun Int?.orZero() = this ?: 0



@Composable
fun ImprovedQuickActionButton(
    data: QuickActionData
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp) // تغییر اندازه دایره به 45.dp
                .shadow(6.dp, CircleShape) // تغییر سایه به 6.dp
                .clip(CircleShape)
                .background(
                    color = if (data.gradient || data.isActive) Color(0xFF6200EA) // رنگ جدید دایره
                    else Color(0xFFF1F1F1), // رنگ پیش‌فرض دایره
                    shape = CircleShape
                )
                .clickable(onClick = data.onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.label,
                tint = if (data.gradient || data.isActive) Color.White // رنگ آیکون در حالت فعال
                else Color(0xFF6200EA), // رنگ آیکون در حالت غیر فعال
                modifier = Modifier.size(28.dp) // تغییر اندازه آیکون به 32.dp
            )
        }

        Text(
            text = data.label,
            style = MaterialTheme.typography.bodySmall , // تغییر استایل به body2
            color = Color(0xFF333333) // رنگ متن تغییر کرده
        )
    }
}


@Composable
private fun VirtualKey(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (text) {
            "⌫" -> Icon(
                imageVector = Icons.AutoMirrored.Rounded.Backspace,
                contentDescription = "Backspace",
                tint = RemoteColors.PrimaryGradientEnd,
                modifier = Modifier.size(20.dp)
            )
            else -> Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = RemoteColors.DarkText
            )
        }
    }
}

@Composable
fun ImprovedVirtualKeyboard(
    onKeyPress: (String) -> Unit,
    maxWidth: Dp,
    isPortrait: Boolean
) {
    val keyboardLayout = remember {
        listOf(
            "1234567890".toList(),
            "QWERTYUIOP".toList(),
            "ASDFGHJKL".toList(),
            "ZXCVBNM⌫".toList()
        )
    }

    Column(
        modifier = Modifier.width(maxWidth),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keyboardLayout.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { key ->
                    VirtualKey(
                        text = key.toString(),
                        onClick = { onKeyPress(key.toString()) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onKeyPress("Space") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = RemoteColors.DarkText
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Space")
            }
            Button(
                onClick = { onKeyPress("Enter") },
                modifier = Modifier.width(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RemoteColors.PrimaryGradientEnd
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Enter", color = Color.White)
            }
        }
    }
}
@Composable
fun ImprovedDPadLayout(
    onUpClick: () -> Unit,
    onDownClick: () -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onCenterClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DPadButton(
                icon = Icons.Rounded.KeyboardArrowUp,
                onClick = onUpClick
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DPadButton(
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    onClick = onLeftClick
                )
                DPadButton(
                    icon = Icons.Rounded.Check,
                    onClick = onCenterClick,
                    isCenter = true
                )
                DPadButton(
                    icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    onClick = onRightClick
                )
            }

            DPadButton(
                icon = Icons.Rounded.KeyboardArrowDown,
                onClick = onDownClick
            )
        }
    }
}


@Composable
fun ImprovedMediaControls(
    controls: List<Triple<ImageVector, String, RemoteKeyCode>>,
    onControlClick: (RemoteKeyCode) -> Unit,
    isPortrait: Boolean = true
) {
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
            controls.forEach { (icon, description, keyCode) ->
                MediaButton(
                    icon = icon,
                    contentDescription = description,
                    isPrimary = description == "Play/Pause",
                    onClick = { onControlClick(keyCode) }
                )
            }
        }
    }
}
@Composable
fun ImprovedTouchpadLayout(
    onSwipe: (String) -> Unit,
    onTap: () -> Unit
) {
    var touchStart by remember { mutableStateOf(Offset.Zero) }
    var touchEnd by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // مربع
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE0E0E0)) // رنگ ملایم‌تر
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        touchStart = offset
                    },
                    onDragEnd = {
                        val dx = touchEnd.x - touchStart.x
                        val dy = touchEnd.y - touchStart.y
                        when {
                            abs(dx) > abs(dy) -> if (dx > 0) onSwipe("right") else onSwipe("left")
                            else -> if (dy > 0) onSwipe("down") else onSwipe("up")
                        }
                    },
                    onDrag = { change, _ ->
                        touchEnd = change.position
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTap() }
                )
            }
    ) {
        Icon(
            imageVector = Icons.Rounded.TouchApp,
            contentDescription = "Touchpad",
            tint = Color.DarkGray,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
        )
    }
}

@Composable
fun ImprovedKeypadLayout(
    onNumberClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (0..9).chunked(3).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { number ->
                        KeypadButton(
                            text = number.toString(),
                            onClick = { onNumberClick(number) },
                            isPrimary = number == 5
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImprovedKeyboardLayout(
    maxWidth: Dp,
    isPortrait: Boolean
) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            maxLines = 1
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ImprovedVirtualKeyboard(
                onKeyPress = { key ->
                    when (key) {
                        "⌫" -> if (inputText.isNotEmpty()) inputText = inputText.dropLast(1)
                        "Space" -> inputText += " "
                        "Enter" -> { /* Handle enter */ }
                        else -> inputText += key
                    }
                },
                maxWidth = maxWidth,
                isPortrait = isPortrait
            )
        }
    }
}