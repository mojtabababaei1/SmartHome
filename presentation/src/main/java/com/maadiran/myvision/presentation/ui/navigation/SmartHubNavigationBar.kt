package com.maadiran.myvision.presentation.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.R

@Composable
fun SmartHubNavigationBar(
    containerColor: Color = Color.White,
    tonalElevation: Dp = 4.dp,
    selectedItem: MutableState<Int>,
    onHomeClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onShoppingClick: () -> Unit,
    onTroubleshootingClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val iconSize = 45.dp
    val itemBackgroundColor = { index: Int ->
        if (selectedItem.value == index) Color(0x220000FF) else Color.Transparent
    }

    NavigationBar(
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    modifier = Modifier
                        .size(iconSize)
                        .background(itemBackgroundColor(0), CircleShape)
                        .padding(6.dp)
                )
            },
            selected = selectedItem.value == 0,
            onClick = {
                selectedItem.value = 0
                onHomeClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_voice),
                    contentDescription = "Voice",
                    modifier = Modifier
                        .size(iconSize)
                        .background(itemBackgroundColor(1), CircleShape)
                        .padding(6.dp)
                )
            },
            selected = selectedItem.value == 1,
            onClick = {
                selectedItem.value = 1
                onVoiceClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_shoping),
                    contentDescription = "Shopping",
                    modifier = Modifier
                        .size(iconSize)
                        .background(itemBackgroundColor(2), CircleShape)
                        .padding(6.dp)
                )
            },
            selected = selectedItem.value == 2,
            onClick = {
                selectedItem.value = 2
                onShoppingClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_troubleshooting),
                    contentDescription = "Troubleshooting",
                    modifier = Modifier
                        .size(iconSize)
                        .background(itemBackgroundColor(3), CircleShape)
                        .padding(6.dp)
                )
            },
            selected = selectedItem.value == 3,
            onClick = {
                selectedItem.value = 3
                onTroubleshootingClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(iconSize)
                        .background(itemBackgroundColor(4), CircleShape)
                        .padding(6.dp)
                )
            },
            selected = selectedItem.value == 4,
            onClick = {
                selectedItem.value = 4
                onProfileClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray
            )
        )
    }
}

