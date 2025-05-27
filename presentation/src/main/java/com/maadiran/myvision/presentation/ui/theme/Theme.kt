import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Smart Home Static Colors
 */
object SmartColors {
    val PrimaryGradientStart = Color(0xFFB7A8F8)
    val PrimaryGradientEnd = Color(0xFF7052F2)
    val SecondaryColor = Color(0xFFF8F7FD)
    val AccentColor = Color(0xFF7052F2).copy(alpha = 0.08f)
    val Background = Color.White
    val TextPrimary = Color.Black
    val TextSecondary = Color.Gray

    // Status Colors
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFFA000)
    val Error = Color(0xFFF44336)
    val Info = Color(0xFF2196F3)

    // Device-specific colors
    val DeviceActive = Success
    val DeviceInactive = TextSecondary
    val DeviceWarning = Warning
}

/**
 * Smart Home Dimensions System
 */
object SmartDimensions {
    // Corner Radius
    val cornerRadiusLarge = 24.dp
    val cornerRadiusMedium = 20.dp
    val cornerRadiusSmall = 16.dp

    // Padding
    val paddingLarge = 32.dp
    val paddingMedium = 16.dp
    val paddingSmall = 8.dp
    val paddingTiny = 4.dp

    // Component Sizes
    val buttonHeight = 66.dp
    val iconSize = 24.dp
    val iconSizeLarge = 32.dp

    // Card Dimensions
    val cardWidth = 160.dp
    val cardHeightSmall = 120.dp
    val cardHeightMedium = 180.dp
    val cardHeightLarge = 240.dp

    // Feature Card Specific
    val featureCardWidth = cardWidth
    val featureCardHeight = cardHeightSmall
}

/**
 * Smart Home Typography System
 */
object SmartTypography {
    val headingLarge = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
    val headingMedium = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    val headingSmall = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )
    val bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )
    val bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    val bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
    val caption = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal
    )
}

/**
 * Smart Home Component Colors
 */
object SmartComponentColors {
    // Card Colors
    @Composable
    fun cardColors() = CardDefaults.cardColors(
        containerColor = SmartColors.SecondaryColor,
        contentColor = SmartColors.TextPrimary,
    )

    // Text Field Colors
    @Composable
    fun textFieldColors() = TextFieldDefaults.colors(
        focusedContainerColor = SmartColors.SecondaryColor,
        unfocusedContainerColor = SmartColors.SecondaryColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedLabelColor = SmartColors.PrimaryGradientEnd,
        unfocusedLabelColor = SmartColors.TextSecondary,
        cursorColor = SmartColors.PrimaryGradientEnd
    )

    // Switch Colors
    @Composable
    fun switchColors() = SwitchDefaults.colors(
        checkedThumbColor = SmartColors.PrimaryGradientEnd,
        checkedTrackColor = SmartColors.PrimaryGradientStart,
        uncheckedThumbColor = SmartColors.TextSecondary,
        uncheckedTrackColor = SmartColors.TextSecondary.copy(alpha = 0.5f)
    )

    // Slider Colors
    @Composable
    fun sliderColors() = SliderDefaults.colors(
        thumbColor = SmartColors.PrimaryGradientEnd,
        activeTrackColor = SmartColors.PrimaryGradientEnd,
        inactiveTrackColor = SmartColors.AccentColor
    )

    // Navigation Colors
    @Composable
    fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
        selectedIconColor = SmartColors.PrimaryGradientEnd,
        selectedTextColor = SmartColors.PrimaryGradientEnd,
        unselectedIconColor = SmartColors.TextSecondary,
        unselectedTextColor = SmartColors.TextSecondary,
        indicatorColor = Color.Transparent
    )

    // Top App Bar Colors
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun topAppBarColors() = TopAppBarDefaults.topAppBarColors(
        containerColor = SmartColors.Background,
        titleContentColor = SmartColors.TextPrimary,
        navigationIconContentColor = SmartColors.TextPrimary,
        actionIconContentColor = SmartColors.TextPrimary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
object SmartComponentDefaults {
    @Composable
    fun topBar(
        title: String,
        navigationIcon: @Composable (() -> Unit)? = null,
        actions: @Composable (RowScope.() -> Unit) = {}
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = title) },
            navigationIcon = { navigationIcon?.invoke() },
            actions = actions,
            colors = SmartComponentColors.topAppBarColors()
        )
    }

    @Composable
    fun deviceScreenContent(
        title: String,
        onNavigateBack: () -> Unit,
        content: @Composable () -> Unit
    ) {
        Scaffold(
            topBar = {
                topBar(
                    title = title,
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SmartColors.Background)
                    .padding(paddingValues)
                    .padding(horizontal = SmartDimensions.paddingMedium)
            ) {
                content()
            }
        }
    }

    @Composable
    fun card(
        title: String,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Card(
            modifier = modifier,
            colors = SmartComponentColors.cardColors(),
            shape = RoundedCornerShape(SmartDimensions.cornerRadiusLarge)
        ) {
            Column(
                modifier = Modifier.padding(SmartDimensions.paddingMedium)
            ) {
                if (title.isNotEmpty()) {
                    Text(
                        text = title,
                        style = SmartTypography.headingSmall,
                        color = SmartColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(SmartDimensions.paddingMedium))
                }
                content()
            }
        }
    }

    @Composable
    fun primaryButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        content: @Composable RowScope.() -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = SmartColors.PrimaryGradientEnd,
                contentColor = Color.White,
                disabledContainerColor = SmartColors.TextSecondary.copy(alpha = 0.12f),
                disabledContentColor = SmartColors.TextSecondary
            ),
            shape = RoundedCornerShape(SmartDimensions.cornerRadiusLarge),
            content = content
        )
    }

    @Composable
    fun secondaryButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        content: @Composable RowScope.() -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = SmartColors.SecondaryColor,
                contentColor = SmartColors.PrimaryGradientEnd,
                disabledContainerColor = SmartColors.TextSecondary.copy(alpha = 0.12f),
                disabledContentColor = SmartColors.TextSecondary
            ),
            shape = RoundedCornerShape(SmartDimensions.cornerRadiusLarge),
            content = content
        )
    }

    @Composable
    fun statusIndicator(
        status: Boolean,
        label: String,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(SmartDimensions.paddingSmall)
        ) {
            Box(
                modifier = Modifier
                    .size(SmartDimensions.paddingSmall * 1.5f)
                    .clip(CircleShape)
                    .background(if (status) SmartColors.Success else SmartColors.Error)
            )
            Text(
                text = label,
                style = SmartTypography.bodyMedium,
                color = SmartColors.TextSecondary
            )
        }
    }

    @Composable
    fun gradientModifier(modifier: Modifier = Modifier): Modifier {
        return modifier.background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    SmartColors.PrimaryGradientStart,
                    SmartColors.PrimaryGradientEnd
                )
            )
        )
    }
}


/**
 * Sign In Specific Theme
 */
object SignInTheme {
    object Colors {
        val PrimaryGradientStart = Color(0xFFB7A8F8)
        val PrimaryGradientEnd = Color(0xFF7052F2)
        val SecondaryColor = Color(0xFFDADAFD)  // Original lighter purple for sign-in cards
        val AccentColor = Color(0xFF7052F2).copy(alpha = 0.1f)  // Original 10% opacity
        val TextPrimary = Color.Black
        val TextSecondary = Color.Gray
        val Background = Color.White
    }

    object Dimensions {
        // Corner Radius
        val cornerRadiusLarge = 24.dp
        val cornerRadiusMedium = 20.dp
        val cornerRadiusSmall = 16.dp

        // Padding
        val paddingLarge = 32.dp
        val paddingMedium = 16.dp
        val paddingSmall = 8.dp
        val paddingTiny = 4.dp

        // Component Sizes
        val buttonHeight = 66.dp
        val iconSize = 24.dp
        val iconSizeLarge = 32.dp
        val featureCardWidth = 160.dp
        val featureCardHeight = 120.dp
    }

    object Typography {
        val headingLarge = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        val headingMedium = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        val bodyLarge = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        val bodyMedium = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        val bodySmall = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }

    @Composable
    fun textFieldColors() = TextFieldDefaults.colors(
        focusedContainerColor = Colors.SecondaryColor,
        unfocusedContainerColor = Colors.SecondaryColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedLabelColor = Colors.PrimaryGradientEnd,
        unfocusedLabelColor = Colors.TextSecondary
    )

    val textFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = Dimensions.paddingSmall)
        .clip(RoundedCornerShape(Dimensions.cornerRadiusMedium))
        .background(Colors.SecondaryColor)

    val gradientButtonModifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Colors.PrimaryGradientStart,
                    Colors.PrimaryGradientEnd
                )
            ),
            shape = RoundedCornerShape(Dimensions.cornerRadiusLarge)
        )

    val socialButtonModifier = Modifier
        .clip(RoundedCornerShape(Dimensions.cornerRadiusMedium))
        .background(Colors.SecondaryColor)

    val bottomNavModifier = Modifier
        .fillMaxWidth()
        .clip(
            RoundedCornerShape(
                topStart = Dimensions.cornerRadiusLarge,
                topEnd = Dimensions.cornerRadiusLarge
            )
        )

    val carouselAnimationSpec = infiniteRepeatable<Float>(
        animation = tween(
            durationMillis = 15000,
            easing = LinearEasing
        ),
        repeatMode = RepeatMode.Restart
    )

    @Composable
    fun GradientButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable RowScope.() -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(Dimensions.cornerRadiusLarge)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Colors.PrimaryGradientStart,
                                Colors.PrimaryGradientEnd
                            )
                        ),
                        shape = RoundedCornerShape(Dimensions.cornerRadiusLarge)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row { content() }
            }
        }
    }
}



