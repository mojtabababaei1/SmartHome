package com.maadiran.myvision.presentation.features.sandbox.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


// VoiceCommandFAB.kt
@Composable
fun VoiceCommandFAB(
    isListening: Boolean,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = if (isListening)
            MaterialTheme.colorScheme.error
        else
            MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = if (isListening)
                Icons.Default.Mic
            else
                Icons.Default.MicNone,
            contentDescription = if (isListening)
                "Stop listening"
            else
                "Start voice command"
        )
    }
}
