package com.maadiran.myvision.presentation.features.auth
/*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.maadiran.myvision.presentation.R
import com.maadiran.myvision.presentation.ui.navigation.SmartHubNavigationBar
import androidx.compose.material.icons.Icons  
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHubSignUpScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSmartHubSignUpScreen() {
    SmartHubSignUpScreen()
}

@Composable
fun SmartHubSignUpScreen() {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { newValue -> email = newValue },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { newValue -> password = newValue },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) 
                VisualTransformation.None 
            else 
                PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { /* Handle signup */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpPreview() {
    SmartHubSignUpScreen()
}

@Composable
fun InputField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    password: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {}
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDADAFD), RoundedCornerShape(24.dp))
            .padding(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        trailingIcon = if (password) {
            {
                val icon = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
                Icon(
                    imageVector = icon,
                    contentDescription = "Password Toggle",
                    modifier = Modifier.clickable { onPasswordToggle() },
                    tint = Color(0xFFB7A8F8).copy(alpha = 0.1f)
                )
            }
        } else null,
        visualTransformation = if (password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun SocialButton(iconRes: Int, contentDescription: String) {
    IconButton(
        onClick = { /* Handle social sign-up */ },
        modifier = Modifier
            .background(Color(0xFFDADAFD), RoundedCornerShape(24.dp))
            .size(48.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun FeatureHighlightsSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quick Access", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        // Features like "Control Devices", "Create Scenes"
        // Example feature card
        FeatureCard(title = "Control Devices")
        FeatureCard(title = "Create Scenes")
    }
}

@Composable
fun FeatureCard(title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDADAFD)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}
 */