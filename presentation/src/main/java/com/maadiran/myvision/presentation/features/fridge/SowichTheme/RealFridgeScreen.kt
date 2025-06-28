package com.maadiran.myvision.presentation.features.fridge.SowichTheme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.maadiran.myvision.models.AppThemeType
import com.maadiran.myvision.presentation.R
import com.maadiran.myvision.presentation.features.fridge.FridgeTemperatureMonitor
import com.maadiran.myvision.presentation.ui.getBackgroundImageRes


@Composable
fun RealFridgeScreen(
    modifier: Modifier,
    fridgeDoor: String,
    freezeDoor: String,
    fridgeTemp: String,
    freezeTemp: String,
    fridgeFan: String,
    fridgeImageRes: Int,
    fanImageRes: Int,
    navController: NavController
) {
    val isFanOn = fridgeFan.contains("on", ignoreCase = true)
    val rotation = if (isFanOn) {
        val infiniteTransition = rememberInfiniteTransition()
        val animatedRotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
        )
        animatedRotation
    } else 0f

    val fridgeTempInt = fridgeTemp.toIntOrNull() ?: 0
    val freezeTempInt = freezeTemp.toIntOrNull() ?: 0

    val fridgeIcon = when {
        fridgeTempInt < 0 -> R.drawable.ic_cold
        fridgeTempInt in 0..10 -> R.drawable.ic_normal_ice
        else -> R.drawable.ic_hot
    }

    val freezerIcon = when {
        freezeTempInt < -10 -> R.drawable.ic_super_ice
        freezeTempInt in -10..0 -> R.drawable.ic_cold
        else -> R.drawable.ic_warning
    }

    val backgroundImageRes = getBackgroundImageRes(AppThemeType.Fantasy)


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = backgroundImageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(id = fridgeImageRes),
            contentDescription = "Fridge",
            modifier = Modifier
                .size(280.dp, 360.dp)
                .align(Alignment.TopCenter)
                .padding(top = 90.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Settings",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(32.dp)
                .clickable { navController.navigate("settings") }
        )

        Image(
            painter = painterResource(id = fanImageRes),
            contentDescription = "Fan",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.BottomCenter)
                .offset(y = -150.dp)
                .graphicsLayer { rotationZ = rotation }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 200.dp)
                .offset(y = -150.dp)
        ) {
            Image(painterResource(id = fridgeIcon), null, Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Fridge: $fridgeTemp°C", fontSize = 16.sp, color = Color.Black)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(end = 200.dp)
                .offset(y = -150.dp)
        ) {
            Image(painterResource(id = freezerIcon), null, Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Freezer: $freezeTemp°C", fontSize = 16.sp, color = Color.Black)
        }

        Text(
            text = "Fridge Door: $fridgeDoor | Freezer Door: $freezeDoor",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 200.dp)
                .size(320.dp, 160.dp)
        ) {
            FridgeTemperatureMonitor()
        }
    }
}
