package com.maadiran.myvision.presentation.features.fridge

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun FridgeStatusScreen(
    getStatus: () -> String
) {
    var response by remember { mutableStateOf("هنوز هیچ اطلاعاتی دریافت نشده") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    response = "در حال دریافت..."
                    response = withContext(Dispatchers.IO) {
                        getStatus()
                    }
                    isLoading = false
                }
            },
            enabled = !isLoading
        ) {
            Text("دریافت اطلاعات یخچال")
        }

        Text(text = response)
    }
}
