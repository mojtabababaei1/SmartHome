package com.maadiran.myvision.presentation.features.fridge.SowichTheme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun FantasyFridgeScreen(
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
            contentDescription = "Fantasy Fridge",
            modifier = Modifier
                .size(280.dp, 360.dp)
                .align(Alignment.TopCenter)
                .padding(top = 90.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "Settings",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .size(40.dp)
                .clickable { navController.navigate("settings") }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 165.dp) // It shifts the entire row
        ) {


            Text(
                text = "Fan cooling:",
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(150.dp)) //The space between the image and the text

            Image(
                painter = painterResource(id = fanImageRes),
                contentDescription = "Fan",
                modifier = Modifier
                    .size(50.dp)
                    .graphicsLayer { rotationZ = rotation } // Fan rotation
            )


        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier

                .align(Alignment.Center)
                .offset(y =100.dp)
                .offset(x =0.dp)

        ) {
            Text(
                text = "Refrigerator Temp",
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = fridgeTemp,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.width(50.dp)) //The space between the image and the text
            Image(painterResource(id = fridgeIcon), null, Modifier.size(40.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y =50.dp)
                .offset(x =0.dp)
        ) {
//
            Text(
                text = "freeze Temp",
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(80.dp)) //The space between the image and the text

            Text(
                text = freezeTemp,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.width(50.dp)) //The space between the image and the text
            Image(painterResource(id = freezerIcon), null, Modifier.size(40.dp))
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

