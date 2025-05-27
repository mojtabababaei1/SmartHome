package com.maadiran.myvision.presentation.features.auth// Necessary imports
/*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.R

// Main composable function
@Preview
@Composable
fun SignUpSignInScreen() {
    // Define colors and gradients
    val primaryGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFB7A8F8), Color(0xFF7052F2))
    )
    val secondaryColor = Color(0xFFDADAFD)
    val accentColor = Color(0xFF7052F2).copy(alpha = 0.1f)
    val inactiveIconColor = Color(0xFFDADAFD)
    val activeIconGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFB7A8F8), Color(0xFF7052F2))
    )

    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                inactiveIconColor = inactiveIconColor,
                activeIconGradient = activeIconGradient
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Email Field
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = secondaryColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color(0xFF7052F2).copy(alpha = 0.1f)
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = secondaryColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Sign In Button
                Button(
                    onClick = { /* Handle sign-in */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(primaryGradient, shape = RoundedCornerShape(50.dp))
                ) {
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Social Sign-Up Options
                Text(text = "Or continue with", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialSignUpButton(
                        iconRes = R.drawable.ic_email, // Replace with your actual resource ID
                        onClick = { /* Handle Email sign-up */ },
                        secondaryColor = secondaryColor
                    )
                    SocialSignUpButton(
                        iconRes = R.drawable.ic_apple, // Replace with your actual resource ID
                        onClick = { /* Handle Apple sign-up */ },
                        secondaryColor = secondaryColor
                    )
                    SocialSignUpButton(
                        iconRes = R.drawable.ic_google, // Replace with your actual resource ID
                        onClick = { /* Handle Google sign-up */ },
                        secondaryColor = secondaryColor
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Feature Highlights Section
                FeatureHighlightsSection(secondaryColor = secondaryColor)
            }
        }
    )
}

// Social Sign-Up Button Composable
@Composable
fun SocialSignUpButton(iconRes: Int, onClick: () -> Unit, secondaryColor: Color) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier.size(56.dp),
        contentPadding = PaddingValues()
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Feature Highlights Section Composable
@Composable
fun FeatureHighlightsSection(secondaryColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Quick Access", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureCard(title = "Control Devices", secondaryColor = secondaryColor)
            FeatureCard(title = "Create Scenes", secondaryColor = secondaryColor)
            // Add more FeatureCards as needed
        }
    }
}

// Feature Card Composable
@Composable
fun FeatureCard(title: String, secondaryColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = secondaryColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(150.dp, 100.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Bottom Navigation Bar Composable
@Composable
fun BottomNavigationBar(inactiveIconColor: Color, activeIconGradient: Brush) {
    val items = listOf("Devices", "Scenes", "Group", "Settings")
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = {
                    val icon = when (item) {
                        "Devices" -> Icons.Default.Devices
                        "Scenes" -> Icons.Default.MeetingRoom
                        "Group" -> Icons.Default.Group
                        "Settings" -> Icons.Default.Settings
                        else -> Icons.Default.Help
                    }
                    if (selectedItem == index) {
                        Icon(
                            imageVector = icon,
                            contentDescription = item,
                            modifier = Modifier
                                .size(24.dp)
                                .drawBehind { drawRect(brush = activeIconGradient) }
                        )
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = item,
                            tint = inactiveIconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem == index) Color.Unspecified else inactiveIconColor
                    )
                }
            )
        }
    }
}
*/