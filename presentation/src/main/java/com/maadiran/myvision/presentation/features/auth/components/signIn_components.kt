package com.maadiran.myvision.presentation.features.auth.components
/*
import SignInTheme
import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DevicesOther
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SocialSignInButton2(
    icon: ImageVector,
    text: String
) {
    Button(
        onClick = { /* Handle social sign in */ },
        modifier = SignInTheme.socialButtonModifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = SignInTheme.Colors.SecondaryColor
        ),
        shape = RoundedCornerShape(SignInTheme.Dimensions.cornerRadiusMedium)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(SignInTheme.Dimensions.paddingSmall)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SignInTheme.Colors.Background)
                    .padding(SignInTheme.Dimensions.paddingSmall)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SignInTheme.Colors.PrimaryGradientEnd,
                    modifier = Modifier.size(SignInTheme.Dimensions.iconSize)
                )
            }
            Spacer(modifier = Modifier.height(SignInTheme.Dimensions.paddingTiny))
            Text(
                text = text,
                style = SignInTheme.Typography.bodySmall.copy(color = SignInTheme.Colors.TextSecondary)
            )
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    icon: ImageVector,
    index: Int,
    cardWidth: Dp = SignInTheme.Dimensions.featureCardWidth,
    containerWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
) {
    var animatedProgress by remember { mutableFloatStateOf(0f) }
    val cardWidthPx = with(LocalDensity.current) { cardWidth.toPx() }
    val containerWidthPx = with(LocalDensity.current) { containerWidth.toPx() }

    LaunchedEffect(Unit) {
        animatedProgress = index * cardWidthPx

        animate(
            initialValue = animatedProgress,
            targetValue = -cardWidthPx,
            animationSpec = SignInTheme.carouselAnimationSpec
        ) { value, _ ->
            animatedProgress = when {
                value < -cardWidthPx -> containerWidthPx
                else -> value
            }
        }
    }

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(SignInTheme.Dimensions.featureCardHeight)
            .offset(x = animatedProgress.dp),
        colors = CardDefaults.cardColors(containerColor = SignInTheme.Colors.SecondaryColor),
        shape = RoundedCornerShape(SignInTheme.Dimensions.cornerRadiusLarge)
    ) {
        Column(
            modifier = Modifier
                .padding(SignInTheme.Dimensions.paddingMedium)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SignInTheme.Colors.Background)
                    .padding(SignInTheme.Dimensions.paddingMedium)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SignInTheme.Colors.PrimaryGradientEnd,
                    modifier = Modifier.size(SignInTheme.Dimensions.iconSizeLarge)
                )
            }
            Spacer(modifier = Modifier.height(SignInTheme.Dimensions.paddingSmall))
            Text(
                text = title,
                style = SignInTheme.Typography.bodyMedium
            )
        }
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
 */