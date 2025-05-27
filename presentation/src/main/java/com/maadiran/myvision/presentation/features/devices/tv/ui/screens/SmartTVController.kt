package com.maadiran.myvision.presentation.features.devices.tv.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.Gamepad
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Input
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.LocalMovies
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Power
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maadiran.myvision.domain.model.RemoteKeyCode
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.domain.services.VoiceState
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.AppShortcut
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedAppShortcuts
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedControlModeSelector
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedDPadLayout
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedKeyboardLayout
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedKeypadLayout
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedQuickActionsGrid
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedTopBar
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.ImprovedTouchpadLayout
import com.maadiran.myvision.presentation.features.devices.tv.ui.components.VolumeControlDialog
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.ControlMode
import com.maadiran.myvision.presentation.features.devices.tv.ui.models.QuickActionData
import com.maadiran.myvision.presentation.features.devices.tv.ui.preview.PreviewAndroidRemoteFactory
import com.maadiran.myvision.presentation.features.devices.tv.ui.preview.PreviewRemoteManager
import com.maadiran.myvision.presentation.features.devices.tv.ui.preview.PreviewVoiceServiceManager
import com.maadiran.myvision.presentation.features.devices.tv.ui.screens.voice.VoiceSelectionDialog
import com.maadiran.myvision.presentation.features.devices.tv.ui.theme.RemoteColors
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel


@Composable
fun SmartTvController(
    viewModel: MainViewModel,
    voiceServiceManager: IVoiceServiceManager, // Updated to use interface
    modifier: Modifier = Modifier
) {
    var selectedInput by remember { mutableStateOf("HDMI 1") }
    var volume by remember { mutableStateOf(50f) }
    var isMuted by remember { mutableStateOf(false) }
    var controlMode by remember { mutableStateOf(ControlMode.DPAD) }
    var isVoiceControlActive by remember { mutableStateOf(false) }
    var showVolumeDialog by remember { mutableStateOf(false) }
    var showVoiceSelectionDialog by remember { mutableStateOf(false) }
    val currentVoiceState by voiceServiceManager.currentVoiceState.collectAsState()
    val recognitionText by voiceServiceManager.recognitionText.collectAsState()
    var isListening by remember { mutableStateOf(false) }
    // سایر متغیرها ...

    // این تابع رو اینجا بنویس
    fun startListening(
        voiceServiceManager: IVoiceServiceManager,
        onResult: (String) -> Unit,
        onError: () -> Unit
    ) {
        Log.d("VoiceDebug", "StartListening clicked") // اینو اضافه کن

        voiceServiceManager.startVoskVoice()
        voiceServiceManager.startListening(onResult, onError)
    }



    // Quick access actions mapping to RemoteKeyCode
    val quickActions = listOf(
        QuickActionData(
            icon = Icons.Rounded.Power,
            label = "Power",
            gradient = true,
            onClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_POWER) }
        ),
        QuickActionData(
            icon = Icons.Rounded.Home,
            label = "Home",
            onClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_HOME) }
        ),
        QuickActionData(
            icon = Icons.AutoMirrored.Rounded.VolumeUp,
            label = "Volume",
            onClick = { showVolumeDialog = true }
        ),
        // Voice control button
        QuickActionData(
            icon = Icons.Rounded.Mic,
            label = "Voice",
            isActive = currentVoiceState != VoiceState.Idle, // Updated to use domain VoiceState
            onClick = {
                showVoiceSelectionDialog = true
            }
        ),
        QuickActionData(
            icon = Icons.Rounded.Input,
            label = "Input",
            onClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_TV_INPUT) }
        ),
        QuickActionData(
            icon = Icons.Rounded.Settings,
            label = "Settings",
            onClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_SETTINGS) }
        )
    )
    // Media control actions
    val mediaActions = listOf(
        Triple(Icons.Rounded.SkipPrevious, "Previous", RemoteKeyCode.KEYCODE_MEDIA_PREVIOUS),
        Triple(Icons.Rounded.Replay10, "Rewind", RemoteKeyCode.KEYCODE_MEDIA_REWIND),
        Triple(Icons.Rounded.PlayArrow, "Play/Pause", RemoteKeyCode.KEYCODE_MEDIA_PLAY_PAUSE),
        Triple(Icons.Rounded.Forward10, "Forward", RemoteKeyCode.KEYCODE_MEDIA_FAST_FORWARD),
        Triple(Icons.Rounded.SkipNext, "Next", RemoteKeyCode.KEYCODE_MEDIA_NEXT)
    )

    // App shortcuts with their corresponding launch commands
    val appShortcuts = listOf(
        AppShortcut(
            name = "filimo",
            icon = Icons.Rounded.Movie,
            backgroundColor = Color(0xFFE50914),
            onClick = { viewModel.sendAppLink("https://www.filimo.com/") }
        ),
        AppShortcut(
            name = "aparat",
            icon = Icons.Rounded.PlayCircle,
            backgroundColor = Color(0xFFFF0000),
            onClick = { viewModel.sendAppLink("https://www.aparat.com/") }
        ),
        AppShortcut(
            name = "Prime",
            icon = Icons.Rounded.LocalMovies,
            backgroundColor = Color(0xFF00A8E1),
            onClick = { viewModel.sendAppLink("https://www.primevideo.com") }
        ),
        // Add more app shortcuts as needed
    )

    // Voice selection dialog
    if (showVoiceSelectionDialog) {
        VoiceSelectionDialog(
            onDismiss = {
                showVoiceSelectionDialog = false
            },
            onVoskSelected = {
                voiceServiceManager.startVoskVoice()
            },
            onGoogleSelected = {
                voiceServiceManager.startGoogleVoice()
            },
            onStopVoice = {
                voiceServiceManager.stopAllVoiceServices()
            },
            currentState = currentVoiceState,
            recognitionText = recognitionText
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 16.dp)
                .systemBarsPadding()
        ) {
            ImprovedTopBar(
                onBackClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_BACK) },
                onSettingsClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_SETTINGS) },
                selectedInput = selectedInput,
                onInputChange = {
                    selectedInput = it
                    when (it) {
                        "HDMI 1" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_TV_INPUT_HDMI_1)
                        "HDMI 2" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_TV_INPUT_HDMI_2)
                        "HDMI 3" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_TV_INPUT_HDMI_3)
                        "HDMI 4" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_TV_INPUT_HDMI_4)
                    }
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // App Shortcuts
                item {
                    ImprovedAppShortcuts(
                        shortcuts = appShortcuts,
                        isPortrait = true
                    )
                }

                // Control Mode Selection
                item {
                    ImprovedControlModeSelector(
                        selectedMode = controlMode,
                        onModeSelect = { mode: ControlMode ->
                            controlMode = mode
                        },
                        isPortrait = true
                    )
                }

                // Main Control Area with Volume
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Main Control (D-Pad, etc)
                        Box(modifier = Modifier.weight(1f)) {
                            when (controlMode) {
                                ControlMode.DPAD -> {
                                    ImprovedDPadLayout(
                                        onUpClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_UP) },
                                        onDownClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_DOWN) },
                                        onLeftClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_LEFT) },
                                        onRightClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_RIGHT) },
                                        onCenterClick = { viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_CENTER) }
                                    )

                                    // میکروفن بعد از DPad
                                    Spacer(modifier = Modifier.height(16.dp))

                                    IconButton(
                                        onClick = {
                                            if (!isListening) {
                                                isListening = true
                                                voiceServiceManager.startListening(
                                                    onResult = { recognizedText ->
                                                        isListening = false
                                                        when (recognizedText.lowercase()) {
                                                            "چپ" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_LEFT)
                                                            "راست" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_RIGHT)
                                                            "بالا" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_UP)
                                                            "پایین" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_DOWN)
                                                            "تایید", "اوکی", "ok" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_CENTER)
                                                        }
                                                    },
                                                    onError = {
                                                        isListening = false
                                                    }
                                                )
                                            }
                                        }
                                    ) {

                                    }
                                }

                                ControlMode.TOUCHPAD -> ImprovedTouchpadLayout(
                                    onSwipe = { direction ->
                                        when (direction) {
                                            "up" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_UP)
                                            "down" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_DOWN)
                                            "left" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_LEFT)
                                            "right" -> viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_RIGHT)
                                        }
                                    },
                                    onTap = { viewModel.sendKey(RemoteKeyCode.KEYCODE_DPAD_CENTER) }
                                )

                                ControlMode.KEYPAD -> ImprovedKeypadLayout(
                                    onNumberClick = { number ->
                                        val keyCode = when (number) {
                                            in 0..9 -> RemoteKeyCode.valueOf("KEYCODE_$number")
                                            else -> null
                                        }
                                        keyCode?.let { viewModel.sendKey(it) }
                                    }
                                )

                                ControlMode.KEYBOARD -> ImprovedKeyboardLayout(
                                    onKeyPress = { key ->
                                        val keyCode = try {
                                            when (key) {
                                                in "A".."Z" -> RemoteKeyCode.valueOf("KEYCODE_$key")
                                                in "0".."9" -> RemoteKeyCode.valueOf("KEYCODE_$key")
                                                "SPACE" -> RemoteKeyCode.KEYCODE_SPACE
                                                "ENTER" -> RemoteKeyCode.KEYCODE_ENTER
                                                "BACKSPACE" -> RemoteKeyCode.KEYCODE_DEL
                                                else -> null
                                            }
                                        } catch (e: IllegalArgumentException) {
                                            Log.e("KeyError", "Invalid key: $key")
                                            null
                                        }
                                        keyCode?.let {
                                            Log.d("KeyboardDebug", "Sending key: $it")
                                            viewModel.sendKey(it)
                                        }
                                    },
                                    maxWidth = 360.dp,
                                    isPortrait = true
                                )
                            }
                        }
                    }
                }




                // Quick Actions Grid and Media Controls in a single container
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ImprovedQuickActionsGrid(
                            actions = quickActions,
                            isPortrait = true
                        )
//                          بخش مدیا پلیر
//                        ImprovedMediaControls(
//                            controls = mediaActions,
//                            onControlClick = { keyCode ->
//                                viewModel.sendKey(keyCode)
//                            }
//                        )
                    }
                }
            }
/*

*/if (currentVoiceState != VoiceState.Idle) {
            ImprovedVoiceControlOverlay(
                recognitionText = recognitionText,
                onDismiss = {
                    voiceServiceManager.stopAllVoiceServices()
                }
            )
        }

            if (showVolumeDialog) {
                VolumeControlDialog(
                    volume = volume,
                    onVolumeChange = { newVolume ->
                        val oldVolume = volume
                        volume = newVolume
                        if (newVolume > oldVolume) {
                            viewModel.sendKey(RemoteKeyCode.KEYCODE_VOLUME_UP)
                        } else {
                            viewModel.sendKey(RemoteKeyCode.KEYCODE_VOLUME_DOWN)
                        }
                    }
                    ,
                    isMuted = isMuted,
                    onMuteToggle = {
                        isMuted = !isMuted
                        viewModel.sendKey(RemoteKeyCode.KEYCODE_MUTE)
                    },
                    onDismiss = { showVolumeDialog = false }
                )
            }
        }
    }
}

@Composable
private fun ControlModeChip(
    mode: ControlMode,
    selected: Boolean,
    onClick: () -> Unit
) {
    val chipWidth = 100.dp // Fixed width for all chips

    Box(
        modifier = Modifier
            .height(48.dp)
            .width(chipWidth)
            .clip(RoundedCornerShape(24.dp))
            .background(
                color = if (selected) RemoteColors.PrimaryGradientEnd else RemoteColors.SecondaryColor
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
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
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = mode.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) Color.White else RemoteColors.DarkText
            )
        }

        // Add "NEW" badge to the touchpad chip
        if (mode == ControlMode.TOUCHPAD) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .size(24.dp)
                    .background(Color.Red, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NEW",
                    style = MaterialTheme.typography.labelSmall.copy(                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SmartTvControllerPreview() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    SmartTvController(
        viewModel = MainViewModel(
            context = context,
            sharedPreferences = sharedPreferences,
            remoteManager = PreviewRemoteManager(),
            voiceServiceManager = PreviewVoiceServiceManager(),
            androidRemoteFactory = PreviewAndroidRemoteFactory()
        ),
        voiceServiceManager = PreviewVoiceServiceManager()
    )
}

