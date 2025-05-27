package com.maadiran.myvision.presentation.features.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maadiran.myvision.presentation.R

// Extension of MainScreen to handle TV integration

// Then update the MainScreen composable
@Composable
fun MainScreen(
    navController: NavController,
    onTVClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // تصویر پس‌زمینه
        Image(
            painter = painterResource(id = R.drawable.fridge_background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // آیکن‌های بالا (تنظیمات و خروج)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // آیکن خروج
            Image(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = "Logout",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        // TODO: Action for logout
                    }
            )

            // آیکن تنظیمات
            Image(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        navController.navigate("settings")
                        Log.d("MainScreen", "Settings icon clicked")
                    }
            )
        }

    val deviceStatuses by viewModel.deviceStatuses.collectAsState()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            modifier = Modifier
                .padding(top = 150.dp) // فاصله از آیکن‌ها
        ) {

        // Smart TV
        item {
            DeviceCard(
                title = "Smart TV",
                image = painterResource(id = R.drawable.smart_tv),
                onClick = onTVClick,
                connectionStatus = DeviceStatus.Disconnected
            )
        }

        // Washing Machine
        item {
            var checkingConnection by remember { mutableStateOf(false) }

            LaunchedEffect(checkingConnection) {
                if (checkingConnection) {
                    // TODO: Logic for washing machine connection
                    checkingConnection = false
                }
            }

            DeviceCard(
                title = "Washing Machine",
                image = painterResource(id = R.drawable.washing_machine),
                onClick = { checkingConnection = true },
                connectionStatus = deviceStatuses["washing-machine"] ?: DeviceStatus.Disconnected
            )
        }

        // Refrigerator
        item {
            DeviceCard(
                title = "Refrigerator",
                image = painterResource(id = R.drawable.fridge),
                onClick = {
                    navController.navigate("fridgeModule")
                },
                connectionStatus = DeviceStatus.Disconnected
            )
        }
    }

}}

enum class DeviceStatus {
    Connected,
    Disconnected,
    Pairing
}

@Composable
fun DeviceCard(
    title: String,
    image: Painter,
    onClick: () -> Unit,
    connectionStatus: DeviceStatus
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
            .padding(8.dp), // اختیاری برای فاصله داخلی
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = image,
            contentDescription = title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = connectionStatus.name,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

