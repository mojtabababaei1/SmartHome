package com.maadiran.myvision.presentation.features.devices.tv.ui.screens.voice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maadiran.myvision.domain.services.VoiceState

@Composable
fun VoiceSelectionDialog(
    onDismiss: () -> Unit,
    onVoskSelected: () -> Unit,
    onGoogleSelected: () -> Unit,
    onStopVoice: () -> Unit,
    currentState: VoiceState,
    recognitionText: String,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {
            onStopVoice()  // وقتی دیالوگ بسته می‌شود، ضبط صدا متوقف می‌شود
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "انتخاب نوع تشخیص صدا",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                // Current Status
                if (currentState != VoiceState.Idle) {
                    Text(
                        text = when (currentState) {
                            is VoiceState.VoskActive -> "کنترل صوتی محلی فعال"
                            is VoiceState.GoogleActive -> "جستجوی صوتی فعال"
                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Recognition Text
                if (recognitionText.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = recognitionText,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Local Voice Control Button (Vosk)
                ElevatedButton(
                    onClick = {
                        if (currentState is VoiceState.VoskActive) {
                            onStopVoice()  // برای متوقف کردن Vosk
                        } else {
                            onVoskSelected()  // برای انتخاب Vosk
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (currentState is VoiceState.VoskActive)
                            MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)  // تغییر شکل دکمه به گرد
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (currentState is VoiceState.VoskActive)
                                Icons.Rounded.Stop else Icons.Filled.Mic,  // تغییر آیکن به میکروفن در ابتدا
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (currentState is VoiceState.VoskActive) {
                            // اگر ضبط در حال اجرا است، نشانگر سیگنال صوتی اضافه می‌شود
                            Icon(
                                imageVector = Icons.Rounded.CloudOff,
                                contentDescription = "Signal Icon",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            if (currentState is VoiceState.VoskActive)
                                "توقف کنترل صوتی محلی"
                            else "کنترل صوتی محلی"
                        )
                    }
                }

                // Google Voice Search Button
                ElevatedButton(
                    onClick = {
                        if (currentState is VoiceState.GoogleActive) {
                            onStopVoice()  // برای متوقف کردن Google Voice
                        } else {
                            onGoogleSelected()  // برای انتخاب Google Voice
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (currentState is VoiceState.GoogleActive)
                            MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)  // تغییر شکل دکمه به گرد
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (currentState is VoiceState.GoogleActive)
                                Icons.Rounded.Stop else Icons.Rounded.Language,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (currentState is VoiceState.GoogleActive)
                                "توقف جستجوی صوتی"
                            else "جستجوی صوتی"
                        )
                    }
                }

                // Error State Message
                if (currentState is VoiceState.Error) {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Close Button
                TextButton(
                    onClick = {
                        onStopVoice()  // برای متوقف کردن ضبط صدا
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("بستن")
                }
            }
        }
    }
}
