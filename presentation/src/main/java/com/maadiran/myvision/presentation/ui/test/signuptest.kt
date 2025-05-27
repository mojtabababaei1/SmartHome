package com.maadiran.myvision.presentation.ui.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Color System
object SmartColors {
    val PrimaryGradientStart = Color(0xFFB7A8F8)  // Lighter purple
    val PrimaryGradientEnd = Color(0xFF7052F2)    // Deeper purple
    val SecondaryColor = Color(0xFFF8F7FD)        // Very soft purple for cards
    val AccentColor = Color(0xFF7052F2).copy(alpha = 0.08f)  // Ultra-soft purple for interactive elements
    val Background = Color.White
    val TextPrimary = Color.Black
    val TextSecondary = Color.Gray
}

// 1.2 Status Colors
object StatusColors {
    val Success = Color(0xFF4CAF50)      // Green for positive states
    val Warning = Color(0xFFFFA000)      // Amber for warnings
    val Error = Color(0xFFF44336)        // Red for errors
    val Info = Color(0xFF2196F3)         // Blue for information
}

// 2. Typography
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

// 3. Spacing & Dimensions
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
    val cardWidth = 160.dp
    val cardHeightSmall = 120.dp
    val cardHeightMedium = 180.dp
    val cardHeightLarge = 240.dp
}

// 4. Component Standards
// 4.1 Cards
val standardCardModifier = Modifier
    .fillMaxWidth()
    .clip(RoundedCornerShape(SmartDimensions.cornerRadiusLarge))
    .background(SmartColors.SecondaryColor)

// 4.2 Buttons
// Primary Button (Gradient)
val primaryButtonModifier = Modifier
    .height(SmartDimensions.buttonHeight)
    .clip(RoundedCornerShape(SmartDimensions.cornerRadiusLarge))
    .background(
        brush = Brush.horizontalGradient(
            colors = listOf(
                SmartColors.PrimaryGradientStart,
                SmartColors.PrimaryGradientEnd
            )
        )
    )

// Icon Button
val iconButtonModifier = Modifier
    .size(48.dp)
    .clip(CircleShape)
    .background(SmartColors.AccentColor)

// 4.3 Input Fields
@Composable
fun inputFieldColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedContainerColor = SmartColors.SecondaryColor,
    unfocusedContainerColor = SmartColors.SecondaryColor,
    disabledContainerColor = SmartColors.SecondaryColor,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedLabelColor = SmartColors.PrimaryGradientEnd,
    unfocusedLabelColor = SmartColors.TextSecondary,
    disabledLabelColor = SmartColors.TextSecondary,
    cursorColor = SmartColors.PrimaryGradientEnd
)

// 5. Interactive Elements
// 5.1 Switches and Toggles
@Composable
fun switchColors(): SwitchColors = SwitchDefaults.colors(
    checkedThumbColor = SmartColors.PrimaryGradientEnd,
    checkedTrackColor = SmartColors.PrimaryGradientStart,
    uncheckedThumbColor = SmartColors.TextSecondary,
    uncheckedTrackColor = SmartColors.TextSecondary.copy(alpha = 0.5f)
)

// 5.2 Sliders
@Composable
fun sliderColors(): SliderColors = SliderDefaults.colors(
    thumbColor = SmartColors.PrimaryGradientEnd,
    activeTrackColor = SmartColors.PrimaryGradientEnd,
    inactiveTrackColor = SmartColors.AccentColor
)

// 6. Navigation
// 6.1 Top App Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBarColors(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
    containerColor = SmartColors.Background,
    titleContentColor = SmartColors.TextPrimary,
    navigationIconContentColor = SmartColors.TextPrimary,
    actionIconContentColor = SmartColors.TextPrimary
)

// 6.2 Bottom Navigation
@Composable
fun bottomNavColors(): NavigationBarItemColors = NavigationBarItemDefaults.colors(
    selectedIconColor = SmartColors.PrimaryGradientEnd,
    selectedTextColor = SmartColors.PrimaryGradientEnd,
    unselectedIconColor = SmartColors.TextSecondary,
    unselectedTextColor = SmartColors.TextSecondary,
    indicatorColor = Color.Transparent
)

// 8. Animation Standards
// 8.1 Transitions
val standardTransitionSpec = tween<Float>(
    durationMillis = 300,
    easing = FastOutSlowInEasing
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignupScreen()
        }
    }
}

@Composable
fun SignupScreen() {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SmartColors.Background)
            .padding(SmartDimensions.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            style = SmartTypography.headingLarge,
            color = SmartColors.TextPrimary,
            modifier = Modifier.padding(bottom = SmartDimensions.paddingLarge)
        )

        // Email Field
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email", style = SmartTypography.bodyMedium) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = inputFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmartDimensions.paddingMedium)
        )

        // Password Field
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password", style = SmartTypography.bodyMedium) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            colors = inputFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmartDimensions.paddingMedium)
        )

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPasswordState.value,
            onValueChange = { confirmPasswordState.value = it },
            label = { Text("Confirm Password", style = SmartTypography.bodyMedium) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirm Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            colors = inputFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmartDimensions.paddingLarge)
        )

        Button(
            onClick = { /* TODO: Implement signup logic */ },
            modifier = primaryButtonModifier.fillMaxWidth(),
            contentPadding = PaddingValues(SmartDimensions.paddingMedium)
        ) {
            Text(
                text = "Sign Up",
                style = SmartTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(SmartDimensions.paddingMedium))

        Text(
            text = "Already have an account? Log In",
            style = SmartTypography.bodyMedium,
            color = SmartColors.TextSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen()
}