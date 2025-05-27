// VoiceRecognitionPage.kt
package com.maadiran.myvision.presentation.features.voicecontrol

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecognitionPage(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val responseMessages by viewModel.responseMessages.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("کنترل صوتی") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            reverseLayout = true
        ) {
            items(responseMessages.size) { index ->
                Text(
                    text = responseMessages[responseMessages.size - 1 - index],
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

