import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DevicesOther
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color definitions
val PrimaryGradientStart = Color(0xFFB7A8F8)
val PrimaryGradientEnd = Color(0xFF7052F2)
val SecondaryColor = Color(0xFFDADAFD)
val AccentColor = Color(0xFF7052F2).copy(alpha = 0.1f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartHubSignUpScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Welcome to Smart Hub",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp)
        )

        // Input Fields Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(16.dp),
        ) {
            // Email Field
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SecondaryColor),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SecondaryColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = PrimaryGradientEnd,
                    unfocusedLabelColor = Color.Gray
                ),
                shape = RoundedCornerShape(20.dp)
            )

            // Password Field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AccentColor)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Rounded.Visibility
                            else Icons.Rounded.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = PrimaryGradientEnd
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SecondaryColor),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SecondaryColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = PrimaryGradientEnd,
                    unfocusedLabelColor = Color.Gray
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }

        // Sign In Button
        Button(
            onClick = { /* Handle sign in */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(66.dp),  // Increased from 56.dp to 64.dp
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(PrimaryGradientStart, PrimaryGradientEnd)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Social Sign-Up Options
        Text(
            text = "Or continue with",
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SocialSignInButton(
                icon = Icons.Rounded.Email,
                text = "Email"
            )
            SocialSignInButton(
                icon = Icons.Rounded.Person,
                text = "Apple"
            )
            SocialSignInButton(
                icon = Icons.Rounded.Public,
                text = "Google"
            )
        }

        // Feature Highlights
        Text(
            text = "Features",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 16.dp)
        )

        // Replace the LazyRow with CarouselFeatureCards
        val features = listOf(
            "Control Devices" to Icons.Rounded.DevicesOther,
            "Create Scenes" to Icons.Rounded.WbSunny,
            "Smart Groups" to Icons.Rounded.Group
        )

        CarouselFeatureCards(features = features)
        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            containerColor = Color.White,
            tonalElevation = 8.dp
        ) {
            val items = listOf(
                Pair("Devices", Icons.Rounded.DevicesOther),
                Pair("Scenes", Icons.Rounded.WbSunny),
                Pair("Group", Icons.Rounded.Group),
                Pair("Settings", Icons.Rounded.Settings)
            )

            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (selectedItem == index) AccentColor
                                    else Color.Transparent
                                )
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = item.second,
                                contentDescription = item.first,
                                tint = if (selectedItem == index)
                                    PrimaryGradientEnd
                                else Color.Gray
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.first,
                            fontSize = 12.sp,
                            fontWeight = if (selectedItem == index)
                                FontWeight.Medium
                            else FontWeight.Normal
                        )
                    },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryGradientEnd,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = PrimaryGradientEnd,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun SocialSignInButton(
    icon: ImageVector,
    text: String
) {
    Button(
        onClick = { /* Handle social sign in */ },
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(SecondaryColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = SecondaryColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryGradientEnd,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    icon: ImageVector,
    index: Int,
    cardWidth: Dp = 160.dp,
    containerWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
) {
    var animatedProgress by remember { mutableFloatStateOf(0f) }
    val cardWidthPx = with(LocalDensity.current) { cardWidth.toPx() }
    val containerWidthPx = with(LocalDensity.current) { containerWidth.toPx() }

    LaunchedEffect(Unit) {
        // Initial offset based on card index
        animatedProgress = index * cardWidthPx

        // Continuous animation
        animate(
            initialValue = animatedProgress,
            targetValue = -cardWidthPx,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 15000, // Adjust speed by changing this value
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        ) { value, _ ->
            // Wrap around logic
            animatedProgress = when {
                value < -cardWidthPx -> containerWidthPx
                else -> value
            }
        }
    }

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(120.dp)
            .offset(x = animatedProgress.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PrimaryGradientEnd,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
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
