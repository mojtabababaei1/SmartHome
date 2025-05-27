package com.maadiran.myvision.presentation.features.sandbox.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.R

@Composable
fun GoogleTVControlPage(
    expandedSection: String? = null,
    onExpandChange: (String?) -> Unit = {},
    onPowerClick: () -> Unit = {},
    onUpClick: () -> Unit = {},
    onDownClick: () -> Unit = {},
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
    onSelectClick: () -> Unit = {},
    onVolumeUpClick: () -> Unit = {},
    onVolumeDownClick: () -> Unit = {},
    onChannelUpClick: () -> Unit = {},
    onChannelDownClick: () -> Unit = {},
    onMuteClick: () -> Unit = {},
    onInputSourceClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    onBrowserClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Mute Button at the Top Right
        IconButton(
            onClick = onMuteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .size(50.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                    ),
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.volume_off_24px),
                contentDescription = "Mute Button",
                tint = Color.White
            )
        }
        // Power Button at the Top Left
        IconButton(
            onClick = onPowerClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .size(50.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                    ),
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.power_settings_new_24px),
                contentDescription = "Power Button",
                tint = Color.White
            )
        }
        // Row for Expandable Buttons
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Keypad Button
            IconButton(
                onClick = { onExpandChange("keypad") },
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.keyboard_alt_24px),
                    contentDescription = "Keypad Button",
                    tint = Color.White
                )
            }

            // Voice Control Button
            IconButton(
                onClick = { onExpandChange("voice") },
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.mic_24px),
                    contentDescription = "Voice Control Button",
                    tint = Color.White
                )
            }

            // Touch Pad Button
            IconButton(
                onClick = { onExpandChange("touch") },
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.touch_app_24px),
                    contentDescription = "Touch Pad Button",
                    tint = Color.White
                )
            }
        }

        // Expanded Content for Keypad, Voice Control, or Touch Pad
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = screenHeight * 0.25f)
        ) {
            when (expandedSection) {
                "keypad" -> {
                    // Keypad UI with 0-9 buttons
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE6F7FF), shape = RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            for (i in 1..9 step 3) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    for (j in i until i + 3) {
                                        Button(
                                            onClick = { /* Handle number click */ },
                                            modifier = Modifier.size(screenWidth * 0.1f)
                                        ) {
                                            Text(text = "$j")
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Button(
                                onClick = { /* Handle 0 click */ },
                                modifier = Modifier.size(screenWidth * 0.1f)
                            ) {
                                Text(text = "0")
                            }
                        }
                    }
                }
                "voice" -> {
                    // Voice Control UI
                    Text(text = "Voice Control Expanded", modifier = Modifier.padding(16.dp))
                }
                "touch" -> {
                    // Touch Pad UI
                    Text(text = "Touch Pad Expanded", modifier = Modifier.padding(16.dp))
                }
            }
        }

        // Directional Pad in the Center
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(screenWidth * 0.4f)
        ) {
            // Up Button
            IconButton(
                onClick = onUpClick,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(screenWidth * 0.12f)
                    .background(Color(0xFFE6F7FF), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Up Button",
                    tint = Color.Black
                )
            }

            // Left Button
            IconButton(
                onClick = onLeftClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(screenWidth * 0.12f)
                    .background(Color(0xFFE6F7FF), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Left Button",
                    tint = Color.Black
                )
            }

            // Select Button in the Center
            IconButton(
                onClick = onSelectClick,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(screenWidth * 0.15f)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Select Button",
                    tint = Color.White
                )
            }

            // Right Button
            IconButton(
                onClick = onRightClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(screenWidth * 0.12f)
                    .background(Color(0xFFE6F7FF), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Right Button",
                    tint = Color.Black
                )
            }

            // Down Button
            IconButton(
                onClick = onDownClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(screenWidth * 0.12f)
                    .background(Color(0xFFE6F7FF), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Down Button",
                    tint = Color.Black
                )
            }
        }

        // Container for Volume, Channel, and b1 Box
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Volume Up/Down Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
                    .size(width = screenWidth * 0.12f, height = screenHeight * 0.22f)
                    .background(Color(0xFFE6F7FF), shape = RoundedCornerShape(16.dp))
            ) {
                IconButton(
                    onClick = onVolumeUpClick,
                    modifier = Modifier.size(screenWidth * 0.1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.volume_up_24px),
                        contentDescription = "Volume Up Button",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = onVolumeDownClick,
                    modifier = Modifier.size(screenWidth * 0.1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.volume_down_24px),
                        contentDescription = "Volume Down Button",
                        tint = Color.Black
                    )
                }
            }

            // Box b1 below Directional Pad
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(0.6f)
                    .height(screenHeight * 0.22f)
                    .background(Color(0xFFE6F7FF), shape = RoundedCornerShape(24.dp))
            ) {
                // Input Source Button at Top Left of Box b1
                IconButton(
                    onClick = onInputSourceClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .size(screenWidth * 0.12f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.input_24px),
                        contentDescription = "Input Source Button",
                        tint = Color.White
                    )
                }

                // Settings Button at Bottom Left of Box b1
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .size(screenWidth * 0.12f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings Button",
                        tint = Color.White
                    )
                }
                // Home Button at Center of Box b1
                IconButton(
                    onClick = onHomeClick,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(screenWidth * 0.15f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home_24px),
                        contentDescription = "Home Button",
                        tint = Color.White
                    )
                }

                // Browser Button at Top Right of Box b1
                IconButton(
                    onClick = onBrowserClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(screenWidth * 0.12f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.travel_explore_24px),
                        contentDescription = "Browser Button",
                        tint = Color.White
                    )
                }

                // Play/Pause Button at Bottom Right of Box b1
                IconButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(screenWidth * 0.12f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play/Pause Button",
                        tint = Color.White
                    )
                }
            }

            // Channel Up/Down Controls
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .size(width = screenWidth * 0.12f, height = screenHeight * 0.22f)
                    .background(Color(0xFFE6F7FF), shape = RoundedCornerShape(16.dp))
            ) {
                IconButton(
                    onClick = onChannelUpClick,
                    modifier = Modifier.size(screenWidth * 0.1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Channel Up Button",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = onChannelDownClick,
                    modifier = Modifier.size(screenWidth * 0.1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Channel Down Button",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGoogleTVControlPage() {
    var expandedSection by rememberSaveable { mutableStateOf<String?>(null) }
    GoogleTVControlPage(
        expandedSection = expandedSection,
        onExpandChange = { section -> expandedSection = if (expandedSection == section) null else section },
        onPowerClick = {},
        onUpClick = {},
        onDownClick = {},
        onLeftClick = {},
        onRightClick = {},
        onSelectClick = {},
        onVolumeUpClick = {},
        onVolumeDownClick = {},
        onChannelUpClick = {},
        onChannelDownClick = {},
        onMuteClick = {},
        onInputSourceClick = {},
        onSettingsClick = {},
        onHomeClick = {},
        onPlayPauseClick = {},
        onBrowserClick = {}
    )
}