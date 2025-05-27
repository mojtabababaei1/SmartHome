package com.maadiran.myvision.presentation.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SmartHubNavigationBar(
    containerColor: Color = Color.White, // Set the background color
    tonalElevation: Dp = 4.dp, // Set the elevation
    onHomeClick: () -> Unit,
    onDevicesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00eff4), Color(0xFF02a2ff))
    )

    val selectedItem = remember { mutableStateOf(1) } // Default to Home

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = containerColor,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = tonalElevation,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Devices",
                        tint = if (selectedItem.value == 0) Color.White else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = "Devices",
                        fontSize = 12.sp,
                        color = if (selectedItem.value == 0) Color.White else Color.Gray
                    )
                },
                selected = selectedItem.value == 0,
                onClick = {
                    selectedItem.value = 0
                    onDevicesClick()
                },
                alwaysShowLabel = true,
                modifier = if (selectedItem.value == 0) Modifier
                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                    .padding(8.dp) else Modifier
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        tint = if (selectedItem.value == 1) Color.White else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        fontSize = 12.sp,
                        color = if (selectedItem.value == 1) Color.White else Color.Gray
                    )
                },
                selected = selectedItem.value == 1,
                onClick = {
                    selectedItem.value = 1
                    onHomeClick()
                },
                alwaysShowLabel = true,
                modifier = if (selectedItem.value == 1) Modifier
                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                    .padding(8.dp) else Modifier
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = if (selectedItem.value == 2) Color.White else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = "Settings",
                        fontSize = 12.sp,
                        color = if (selectedItem.value == 2) Color.White else Color.Gray
                    )
                },
                selected = selectedItem.value == 2,
                onClick = {
                    selectedItem.value = 2
                    onSettingsClick()
                },
                alwaysShowLabel = true,
                modifier = if (selectedItem.value == 2) Modifier
                    .background(brush = gradientBrush, shape = RoundedCornerShape(50))
                    .padding(8.dp) else Modifier
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewSmartHubNavigationBar() {
    SmartHubNavigationBar(
        onHomeClick = {},
        onDevicesClick = {},
        onSettingsClick = {}
    )
}
