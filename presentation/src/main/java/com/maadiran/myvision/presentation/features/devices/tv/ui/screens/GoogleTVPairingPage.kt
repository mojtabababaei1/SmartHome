package com.maadiran.myvision.presentation.features.devices.tv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.material3.TextFieldDefaults
import com.maadiran.myvision.presentation.features.devices.tv.viewmodels.MainViewModel
import com.maadiran.myvision.core.model.DeviceInfo

@Preview(showBackground = true)
@Composable
fun GoogleTVPairingPage(
    onPairClick: () -> Unit = {},
    onCodeEntered: (String) -> Unit = {},
    isPairingStarted: Boolean = false,
    onSsdpDiscovery: () -> Unit = {},
    discoveredDevices: StateFlow<List<DeviceInfo>> = MutableStateFlow(emptyList()),
    onDeviceSelected: (DeviceInfo) -> Unit = {},
    onGoToController: () -> Unit = {},
    isPairedPreviously: Boolean = false,
    connectionState: MainViewModel.ConnectionState = MainViewModel.ConnectionState.Disconnected,
    discoveryError: String? = null
) {
    var isDiscovering by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf<DeviceInfo?>(null) }
    var pairingCode by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf<String?>(null) }

    val devices by discoveredDevices.collectAsState()

    LaunchedEffect(devices) {
        if (devices.isNotEmpty()) {
            isDiscovering = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isPairingStarted) {
            Text(
                text = "Pair Your Device",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Discovery Status
            if (isDiscovering) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color(0xFF02a2ff)
                )
                Text(
                    text = "Searching for devices...",
                    color = Color(0xFF02a2ff),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Pair Button
            Button(
                onClick = {
                    isDiscovering = true
                    showError = null
                    onSsdpDiscovery()
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color(0xFFE0E0E0)
                ),
                enabled = !isDiscovering,
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (!isDiscovering) "Search for Devices" else "Searching...",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Display discovered devices
            if (devices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Available Devices",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(devices) { device ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = {
                                selectedDevice = device
                                onDeviceSelected(device)
                            },
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = if (selectedDevice == device)
                                    Color(0xFFE3F2FD) else Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = device.name,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = device.host,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                                if (selectedDevice == device) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color(0xFF02a2ff)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (discoveryError != null) {
                Text(
                    text = discoveryError,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isDiscovering) {
                Button(
                    onClick = onGoToController,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Skip Pairing")
                }
            }

        } else {
            // Pairing code input
            Text(
                text = "Enter Pairing Code",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            selectedDevice?.let { device ->
                Text(
                    text = "Connecting to: ${device.name}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = pairingCode,
                onValueChange = { input ->
                    if (input.length <= 6) {
                        pairingCode = input.uppercase()
                    }
                },
                placeholder = { Text("Enter Code") },
                modifier = Modifier
                    .fillMaxWidth(0.7f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF02a2ff),
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color(0xFF02a2ff)
                )
            )


            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onCodeEntered(pairingCode)
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Submit Code", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
