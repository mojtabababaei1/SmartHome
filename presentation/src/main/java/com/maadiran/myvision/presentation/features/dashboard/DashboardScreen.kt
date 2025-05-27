import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.background(SmartColors.Background),
        topBar = {
            SmartComponentDefaults.topBar(
                title = "Smart Home"
            )
        },
        bottomBar = {
            BottomNavigation(selectedItem = selectedItem) { selectedItem = it }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SmartColors.Background)
                .padding(padding)
                .padding(horizontal = SmartDimensions.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(SmartDimensions.paddingMedium)
        ) {
            // Voice Assistant Card
            item {
                VoiceAssistantCard()
            }

            // Quick Controls Section
            item {
                Text(
                    "Quick Controls",
                    style = SmartTypography.headingMedium
                )
            }

            // TV Control Card
            item {
                TVControlCard()
            }


            // Washing Machine Control Card
            item {
                WashingMachineCard()
            }

            // Cooler Control Card
            item {
                CoolerControlCard()
            }
        }
    }
}

@Composable
fun VoiceAssistantCard() {
    SmartComponentDefaults.card(
        title = "",
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Voice Assistant",
                    style = SmartTypography.headingSmall
                )
                Text(
                    "Hey, how can I help you?",
                    style = SmartTypography.bodyMedium,
                    color = SmartColors.TextSecondary
                )
            }

            IconButton(
                onClick = { /* Activate voice assistant */ },
                modifier = Modifier
                    .size(SmartDimensions.iconSizeLarge)
                    .clip(CircleShape)
                    .background(SmartColors.AccentColor)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Mic,
                    contentDescription = "Activate voice assistant",
                    tint = SmartColors.PrimaryGradientEnd
                )
            }
        }
    }
}

@Composable
fun TVControlCard() {
    var powerState by remember { mutableStateOf(false) }
    var volume by remember { mutableStateOf(0.5f) }

    SmartComponentDefaults.card(
        title = "TV",
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Tv,
                contentDescription = null,
                tint = SmartColors.PrimaryGradientEnd
            )
            Switch(
                checked = powerState,
                onCheckedChange = { powerState = it },
                colors = SmartComponentColors.switchColors()
            )
        }

        if (powerState) {
            Spacer(Modifier.height(SmartDimensions.paddingMedium))
            Text(
                "Volume",
                style = SmartTypography.bodyMedium,
                color = SmartColors.TextSecondary
            )
            Slider(
                value = volume,
                onValueChange = { volume = it },
                colors = SmartComponentColors.sliderColors()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TVControlButton(Icons.Rounded.SkipPrevious, "Previous channel")
                TVControlButton(Icons.Rounded.List, "Channel list")
                TVControlButton(Icons.Rounded.SkipNext, "Next channel")
            }
        }
    }
}

@Composable
fun TVControlButton(icon: ImageVector, contentDescription: String) {
    IconButton(
        onClick = { /* Handle action */ },
        modifier = Modifier
            .size(SmartDimensions.iconSize)
            .clip(CircleShape)
            .background(SmartColors.AccentColor)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = SmartColors.PrimaryGradientEnd
        )
    }
}


@Composable
fun TemperatureRow(label: String, temp: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = SmartTypography.bodyMedium)
        Text(
            temp,
            style = SmartTypography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun WashingMachineCard() {
    SmartComponentDefaults.card(
        title = "Washing Machine",
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CommandButton("Quick Wash", Icons.Rounded.Speed)
            CommandButton("Normal Wash", Icons.Rounded.Wash)
            CommandButton("Heavy Duty", Icons.Rounded.Layers)
        }
    }
}

@Composable
fun CommandButton(label: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { /* Send command */ },
            modifier = Modifier
                .size(SmartDimensions.iconSizeLarge)
                .clip(CircleShape)
                .background(SmartColors.AccentColor)
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = SmartColors.PrimaryGradientEnd
            )
        }
        Spacer(Modifier.height(SmartDimensions.paddingTiny))
        Text(
            label,
            style = SmartTypography.bodySmall,
            color = SmartColors.TextSecondary
        )
    }
}


    @Composable
    fun CoolerControlCard() {
        var isOn by remember { mutableStateOf(false) }
        var temperature by remember { mutableStateOf(24f) }

        SmartComponentDefaults.card(
            title = "Cooler",
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.AcUnit,
                    contentDescription = null,
                    tint = SmartColors.PrimaryGradientEnd
                )
                Switch(
                    checked = isOn,
                    onCheckedChange = { isOn = it },
                    colors = SmartComponentColors.switchColors()
                )
            }

            if (isOn) {
                Spacer(Modifier.height(SmartDimensions.paddingMedium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Temperature",
                        style = SmartTypography.bodyMedium,
                        color = SmartColors.TextSecondary
                    )
                    Text(
                        "${temperature.toInt()}°C",
                        style = SmartTypography.bodyLarge
                    )
                }

                Slider(
                    value = temperature,
                    onValueChange = { temperature = it },
                    valueRange = 16f..30f,
                    colors = SmartComponentColors.sliderColors()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CoolerModeButton("Auto", Icons.Rounded.AutoMode)
                    CoolerModeButton("Cool", Icons.Rounded.AcUnit)
                    CoolerModeButton("Fan", Icons.Rounded.Air)
                    CoolerModeButton("Dry", Icons.Rounded.WaterDrop)
                }
            }
        }
    }

    @Composable
    fun CoolerModeButton(label: String, icon: ImageVector) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { /* Set mode */ },
                modifier = Modifier
                    .size(SmartDimensions.iconSize)
                    .clip(CircleShape)
                    .background(SmartColors.AccentColor)
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = SmartColors.PrimaryGradientEnd
                )
            }
            Spacer(Modifier.height(SmartDimensions.paddingTiny))
            Text(
                label,
                style = SmartTypography.bodySmall,
                color = SmartColors.TextSecondary
            )
        }
    }


@Composable
fun BottomNavigation(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = SignInTheme.bottomNavModifier,
        containerColor = SignInTheme.Colors.Background,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            "Devices" to Icons.Rounded.DevicesOther,
            "Scenes" to Icons.Rounded.WbSunny,
            "Group" to Icons.Rounded.Group,
            "Settings" to Icons.Rounded.Settings
        )

        items.forEachIndexed { index, (title, icon) ->
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (selectedItem == index)
                                    SignInTheme.Colors.AccentColor
                                else SignInTheme.Colors.Background
                            )
                            .padding(SignInTheme.Dimensions.paddingSmall)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title
                        )
                    }
                },
                label = {
                    Text(
                        text = title,
                        style = SignInTheme.Typography.bodySmall
                    )
                },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SignInTheme.Colors.PrimaryGradientEnd,
                    selectedTextColor = SignInTheme.Colors.PrimaryGradientEnd,
                    unselectedIconColor = SignInTheme.Colors.TextSecondary,
                    unselectedTextColor = SignInTheme.Colors.TextSecondary,
                    indicatorColor = SignInTheme.Colors.Background
                )
            )
        }
    }
}