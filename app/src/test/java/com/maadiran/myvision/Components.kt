package com.maadiran.myvision

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TVStatusHeader(selectedInput: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryColor),
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = "Input: $selectedInput",
                    fontSize = 14.sp,
                    color = MediumText
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = AccentColor, shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Cast,
                    contentDescription = "Connected",
                    tint = PrimaryGradientEnd,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@Composable
fun QuickActionsRow(
    isMuted: Boolean,
    onMuteToggle: () -> Unit,
    isVoiceControlActive: Boolean,
    onVoiceControlToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuickActionButton(
            icon = Icons.Rounded.Power,
            label = "Power",
            gradient = true
        )
        QuickActionButton(
            icon = if (isMuted) Icons.Rounded.VolumeOff else Icons.Rounded.VolumeUp,
            label = if (isMuted) "Unmute" else "Mute",
            onClick = onMuteToggle
        )
        QuickActionButton(
            icon = Icons.Rounded.Home,
            label = "Home"
        )
        QuickActionButton(
            icon = Icons.Rounded.Mic,
            label = "Voice",
            isActive = isVoiceControlActive,
            onClick = onVoiceControlToggle
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    gradient: Boolean = false,
    isActive: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .then(
                    if (gradient || isActive) {
                        Modifier.background(brush = primaryGradient, shape = CircleShape)
                    } else {
                        Modifier.background(color = Color.White, shape = CircleShape)
                    }
                )
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (gradient || isActive) Color.White else PrimaryGradientEnd,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = MediumText,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MediaControlsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MediaControlButton(
            icon = Icons.Rounded.SkipPrevious,
            contentDescription = "Previous",
            size = 40.dp
        )
        MediaControlButton(
            icon = Icons.Rounded.Replay10,
            contentDescription = "Rewind",
            size = 40.dp
        )
        MediaControlButton(
            icon = Icons.Rounded.PlayArrow,
            contentDescription = "Play/Pause",
            isPrimary = true,
            size = 52.dp
        )
        MediaControlButton(
            icon = Icons.Rounded.Forward10,
            contentDescription = "Forward",
            size = 40.dp
        )
        MediaControlButton(
            icon = Icons.Rounded.SkipNext,
            contentDescription = "Next",
            size = 40.dp
        )
    }
}

@Composable
fun MediaControlButton(
    icon: ImageVector,
    contentDescription: String,
    isPrimary: Boolean = false,
    size: Dp = 48.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = if (isPrimary) 8.dp else 4.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .then(
                if (isPrimary) {
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
            contentDescription = contentDescription,
            tint = if (isPrimary) Color.White else PrimaryGradientEnd,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}



// Color definitions
val PrimaryGradientStart = Color(0xFFB7A8F8)
val PrimaryGradientEnd = Color(0xFF7052F2)
val SecondaryColor = Color(0xFFDADAFD)
val AccentColor = Color(0xFF7052F2).copy(alpha = 0.1f)
val DarkText = Color.Black.copy(alpha = 0.8f)
val MediumText = Color.Black.copy(alpha = 0.6f)
val LightText = Color.Black.copy(alpha = 0.4f)

// Gradient brush for reuse
val primaryGradient = Brush.linearGradient(
    colors = listOf(PrimaryGradientStart, PrimaryGradientEnd)
)