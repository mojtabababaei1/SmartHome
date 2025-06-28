package com.maadiran.myvision.presentation.features.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maadiran.myvision.models.AppThemeType
import com.maadiran.myvision.presentation.R
import com.maadiran.myvision.presentation.ui.getAirCImageRes
import com.maadiran.myvision.presentation.ui.getBackgroundImageRes
import com.maadiran.myvision.presentation.ui.getRefrigeImageRes
import com.maadiran.myvision.presentation.ui.getTVImageRes
import com.maadiran.myvision.presentation.ui.getWasherImageRes

import com.maadiran.myvision.presentation.ui.theme.ThemeViewModel



@Composable
fun MainScreen(
    navController: NavController,
    onTVClick: () -> Unit,
  //  fridgeModule: () -> Unit,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel,
    viewModel: MainScreenViewModel = hiltViewModel(),

) {
    // تصویر پس‌زمینه
    val currentTheme by themeViewModel.currentTheme.collectAsState()
    val backgroundImageRes = getBackgroundImageRes(currentTheme)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = backgroundImageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // تصویر کل صفحه رو بگیره
        )
        }


    // آیکن‌های بالا (تنظیمات و خروج)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Settings",
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    navController.navigate("settingsTheme")

                }
        )
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
      }

    val deviceStatuses by viewModel.deviceStatuses.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp),
        modifier = Modifier
            .padding(top = 150.dp)
    ) {

        // Smart TV
        item {
            DeviceCard(
                title = "Smart TV",
                image = painterResource(id = getTVImageRes(currentTheme)),
                onClick = onTVClick,
                connectionStatus = DeviceStatus.Disconnected,
                appTheme = currentTheme
            )
        }

        // Air Conditioner
        item {
            DeviceCard(
                title = "Smart Air Conditioner",
                image = painterResource(id = getAirCImageRes(currentTheme)),
                onClick = onTVClick,
                connectionStatus = DeviceStatus.Disconnected,
                appTheme = currentTheme
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
                image = painterResource(id = getWasherImageRes(currentTheme)),
                onClick = onTVClick,
                connectionStatus = DeviceStatus.Disconnected,
                appTheme = currentTheme
            )
        }

        // Refrigerator
        item {
            DeviceCard(
                title = "Smart Refrigerator",
                image = painterResource(id = getRefrigeImageRes(currentTheme)),
                onClick = {
                    navController.navigate("fridgeModule")
                },
                connectionStatus = DeviceStatus.Connected,
                appTheme = currentTheme
            )
            Log.d("TEST", "status = ${DeviceStatus.Connected}")

        }
    }

}

enum class DeviceStatus {
    Connected,
    Disconnected,
    Pairing
}

@Composable
fun ThemeSwitcher(viewModel: ThemeViewModel) {
    val currentTheme by viewModel.currentTheme.collectAsState()

    Button(
        onClick = {
            val newTheme = if (currentTheme == AppThemeType.Real) AppThemeType.Fantasy else AppThemeType.Real
            viewModel.setTheme(newTheme)
        }
    ) {
        Text("Switch to ${if (currentTheme == AppThemeType.Real) "Fantasy" else "Real"} Theme")
    }
}



@Composable
fun DeviceCard(
    title: String,
    image: Painter,
    onClick: () -> Unit,
    connectionStatus: DeviceStatus,
    appTheme: AppThemeType
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() }
            .padding(8.dp),
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

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (appTheme == AppThemeType.Fantasy) Color.Black else Color.White
        )



        val statusIcon = when (connectionStatus) {
            DeviceStatus.Connected -> painterResource(id = R.drawable.ic_connected)
            DeviceStatus.Disconnected -> painterResource(id = R.drawable.ic_disconnected)
            DeviceStatus.Pairing -> painterResource(id = R.drawable.ic_pairing)
        }

        Image(
            painter = statusIcon,
            contentDescription = connectionStatus.name,
            modifier = Modifier.size(24.dp) // آیکون کوچک
        )
    }
}


