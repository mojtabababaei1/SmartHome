package com.maadiran.myvision.presentation.features.devices.tv.ui.screens.voice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.domain.services.IVoiceServiceManager
import com.maadiran.myvision.presentation.features.devices.tv.ui.theme.RemoteColors

@Composable
fun GoogleVoicePage(
    voiceServiceManager: IVoiceServiceManager,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isListening by remember { mutableStateOf(false) }
    val recognitionText by voiceServiceManager.recognitionText.collectAsState()
    val currentVoiceState by voiceServiceManager.currentVoiceState.collectAsState()

    LaunchedEffect(Unit) {
        voiceServiceManager.stopAllVoiceServices()
        voiceServiceManager.startGoogleVoice()
        isListening = true
    }

    DisposableEffect(Unit) {
        onDispose {
            voiceServiceManager.stopAllVoiceServices()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                voiceServiceManager.stopAllVoiceServices()
                onBackClick()
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "جستجوی صوتی",
                style = MaterialTheme.typography.titleLarge
            )

            // Stop both voices button
            IconButton(
                onClick = {
                    voiceServiceManager.stopAllVoiceServices()
                    isListening = false
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MicOff,
                    contentDescription = "Stop Voice Recognition"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Voice Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = recognitionText.ifEmpty { "در انتظار صدا..." },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Microphone Button
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            FilledIconButton(
                onClick = {
                    if (isListening) {
                        voiceServiceManager.stopAllVoiceServices()
                    } else {
                        voiceServiceManager.startGoogleVoice()
                    }
                    isListening = !isListening
                },
                modifier = Modifier.size(64.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isListening)
                        MaterialTheme.colorScheme.error
                    else
                        RemoteColors.PrimaryGradientEnd
                )
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = if (isListening) "Stop Listening" else "Start Listening",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Status Text
        Text(
            text = if (isListening) "در حال گوش دادن..." else "برای شروع دکمه میکروفون را لمس کنید",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}