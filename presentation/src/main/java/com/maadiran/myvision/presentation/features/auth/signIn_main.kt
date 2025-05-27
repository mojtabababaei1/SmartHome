package com.maadiran.myvision.presentation.features.auth
/*
import SignInTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maadiran.myvision.presentation.features.auth.components.BottomNavigation
import com.maadiran.myvision.presentation.features.auth.components.FeatureCard
import com.maadiran.myvision.presentation.features.auth.components.SocialSignInButton2

@Preview(showBackground = true)
@Composable
fun SignInScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SignInTheme.Colors.Background)
            .padding(SignInTheme.Dimensions.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Welcome Back",
            style = SignInTheme.Typography.headingLarge,
            modifier = Modifier.padding(vertical = SignInTheme.Dimensions.paddingLarge)
        )

        // Input Fields Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(SignInTheme.Dimensions.cornerRadiusLarge))
                .background(SignInTheme.Colors.Background)
                .padding(SignInTheme.Dimensions.paddingMedium),
        ) {
            // Email Field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = SignInTheme.textFieldModifier,
                colors = SignInTheme.textFieldColors(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = null,
                        tint = SignInTheme.Colors.PrimaryGradientEnd
                    )
                }
            )

            Spacer(modifier = Modifier.height(SignInTheme.Dimensions.paddingMedium))

            // Password Field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = SignInTheme.Colors.PrimaryGradientEnd
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SignInTheme.Colors.AccentColor)
                            .padding(SignInTheme.Dimensions.paddingTiny)
                    ) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Rounded.Visibility
                            else Icons.Rounded.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = SignInTheme.Colors.PrimaryGradientEnd
                        )
                    }
                },
                modifier = SignInTheme.textFieldModifier,
                colors = SignInTheme.textFieldColors()
            )
        }
        SignInTheme.GradientButton(
            onClick = { /* Handle sign in */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SignInTheme.Dimensions.paddingMedium)
                .height(SignInTheme.Dimensions.buttonHeight)
        ) {
            Text(
                text = "Sign In",
                style = SignInTheme.Typography.bodyLarge.copy(color = Color.White)
            )
        }
        // Social Sign-In Section
        Text(
            text = "Or continue with",
            style = SignInTheme.Typography.bodyMedium.copy(color = SignInTheme.Colors.TextSecondary),
            modifier = Modifier.padding(vertical = SignInTheme.Dimensions.paddingMedium)
        )

        // Social Sign-In Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SocialSignInButton2(
                icon = Icons.Rounded.Email,
                text = "Email"
            )
            SocialSignInButton2(
                icon = Icons.Rounded.Person,
                text = "Apple"
            )
            SocialSignInButton2(
                icon = Icons.Rounded.Public,
                text = "Google"
            )
        }

        // Features Section
        Text(
            text = "Features",
            style = SignInTheme.Typography.headingMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = SignInTheme.Dimensions.paddingMedium)
        )

        // Feature Cards Carousel
        val features = listOf(
            "Control Devices" to Icons.Rounded.DevicesOther,
            "Create Scenes" to Icons.Rounded.WbSunny,
            "Smart Groups" to Icons.Rounded.Group
        )
        CarouselFeatureCards(features = features)

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        BottomNavigation(selectedItem = selectedItem) { selectedItem = it }
    }
}

@Composable
fun CarouselFeatureCards(
    features: List<Pair<String, ImageVector>>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        // Duplicate the list to create seamless loop
        val duplicatedFeatures = features + features

        duplicatedFeatures.forEachIndexed { index, (title, icon) ->
            FeatureCard(
                title = title,
                icon = icon,
                index = index
            )
        }
    }
}
 */