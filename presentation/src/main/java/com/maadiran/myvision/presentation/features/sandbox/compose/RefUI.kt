import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

data class RefrigeratorState(
    val fridgeTemp: Float,
    val freezerTemp: Float,
    val topLeftDoorOpen: Boolean = false,
    val topRightDoorOpen: Boolean = false,
    val bottomLeftDoorOpen: Boolean = false,
    val bottomRightDoorOpen: Boolean = false,
    val doorOpenDuration: Long = 0L // in milliseconds
)

@Composable
fun FluidRefrigeratorMonitor(
    state: RefrigeratorState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Fluid animation phase
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    // Color animation based on door state and duration
    val targetColor = when {
        state.doorOpenDuration > 30000 -> Color.Red // After 30 seconds, full red
        state.anyDoorOpen -> {
            val intensity = (state.doorOpenDuration / 30000f).coerceIn(0f, 1f)
            lerp(Color.Cyan, Color.Red, intensity)
        }
        else -> Color.Cyan
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = ""
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Main refrigerator visualization
        RefrigeratorShape(
            state = state,
            phase = phase,
            color = animatedColor,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        // Status indicator at bottom
        OverallStatus(
            state = state,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun RefrigeratorShape(
    state: RefrigeratorState,
    phase: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .drawBehind {
                // Draw the main refrigerator outline
                drawRefrigeratorOutline()

                // Draw fluid lighting effect
                drawFluidLighting(phase, color)

                // Draw door divisions and handles
                drawDoors(state)
            }
    ) {
        // Temperature displays
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Fridge temperature
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TemperatureDisplay(
                    "Fridge",
                    state.fridgeTemp,
                    isOpen = state.topLeftDoorOpen || state.topRightDoorOpen
                )
            }

            // Freezer temperature
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TemperatureDisplay(
                    "Freezer",
                    state.freezerTemp,
                    isOpen = state.bottomLeftDoorOpen || state.bottomRightDoorOpen
                )
            }
        }
    }
}

private fun DrawScope.drawRefrigeratorOutline() {
    // Main outline
    drawRoundRect(
        color = Color.LightGray,
        size = size,
        cornerRadius = CornerRadius(32.dp.toPx()),
        style = Stroke(width = 2.dp.toPx())
    )

    // Horizontal division between fridge and freezer
    val centerY = size.height * 0.5f
    drawLine(
        color = Color.LightGray,
        start = Offset(0f, centerY),
        end = Offset(size.width, centerY),
        strokeWidth = 2.dp.toPx()
    )

    // Vertical division for doors
    val centerX = size.width * 0.5f
    drawLine(
        color = Color.LightGray,
        start = Offset(centerX, 0f),
        end = Offset(centerX, size.height),
        strokeWidth = 2.dp.toPx()
    )
}

private fun DrawScope.drawFluidLighting(phase: Float, color: Color) {
    val path = Path()
    val waveAmplitude = size.width * 0.05f
    val waveLength = size.width * 0.5f

    path.moveTo(0f, size.height)

    var x = 0f
    while (x < size.width) {
        val y = size.height - (
                sin(x / waveLength * 2f * PI.toFloat() + phase) * waveAmplitude +
                        sin(x / (waveLength * 0.5f) * 2f * PI.toFloat() - phase) * waveAmplitude * 0.5f
                )
        path.lineTo(x, y)
        x += 5f
    }

    path.lineTo(size.width, size.height)
    path.close()

    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(
                color.copy(alpha = 0.1f),
                color.copy(alpha = 0.05f)
            )
        )
    )
}

private fun DrawScope.drawDoors(state: RefrigeratorState) {
    val handleWidth = 4.dp.toPx()
    val handleHeight = 60.dp.toPx()
    val handleOffset = 20.dp.toPx()

    fun drawHandle(x: Float, y: Float, isOpen: Boolean) {
        drawRect(
            color = if (isOpen) Color.Red else Color.Gray,
            topLeft = Offset(x - handleWidth/2, y - handleHeight/2),
            size = Size(handleWidth, handleHeight)
        )
    }

    // Top left door handle
    drawHandle(
        x = size.width * 0.5f - handleOffset,
        y = size.height * 0.25f,
        isOpen = state.topLeftDoorOpen
    )

    // Top right door handle
    drawHandle(
        x = size.width * 0.5f + handleOffset,
        y = size.height * 0.25f,
        isOpen = state.topRightDoorOpen
    )

    // Bottom left door handle
    drawHandle(
        x = size.width * 0.5f - handleOffset,
        y = size.height * 0.75f,
        isOpen = state.bottomLeftDoorOpen
    )

    // Bottom right door handle
    drawHandle(
        x = size.width * 0.5f + handleOffset,
        y = size.height * 0.75f,
        isOpen = state.bottomRightDoorOpen
    )
}

@Composable
private fun TemperatureDisplay(
    label: String,
    temperature: Float,
    isOpen: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (isOpen) Color.Red else Color.DarkGray
        )
        Text(
            text = "${temperature.toInt()}°C",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = if (isOpen) Color.Red else Color.DarkGray
        )
    }
}

@Composable
private fun OverallStatus(
    state: RefrigeratorState,
    modifier: Modifier = Modifier
) {
    val status = when {
        state.doorOpenDuration > 30000 -> "Critical" to Color.Red
        state.anyDoorOpen -> "Warning" to Color(0xFFFF9800)
        else -> "Normal" to Color(0xFF4CAF50)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = status.second.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Overall Status",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = status.first,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = status.second
                    )
                )
            }

            // Temperature summary
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Fridge: ${state.fridgeTemp}°C",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Freezer: ${state.freezerTemp}°C",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

val RefrigeratorState.anyDoorOpen: Boolean
    get() = topLeftDoorOpen || topRightDoorOpen || bottomLeftDoorOpen || bottomRightDoorOpen


@Preview(name = "Normal State", showBackground = true, heightDp = 800)
@Composable
fun NormalStatePreview() {
    MaterialTheme {
        Surface {
            FluidRefrigeratorMonitor(
                state = RefrigeratorState(
                    fridgeTemp = 4f,
                    freezerTemp = -18f,
                    doorOpenDuration = 0L
                ),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "Warning State", showBackground = true, heightDp = 800)
@Composable
fun WarningStatePreview() {
    MaterialTheme {
        Surface {
            FluidRefrigeratorMonitor(
                state = RefrigeratorState(
                    fridgeTemp = 6f,
                    freezerTemp = -16f,
                    topLeftDoorOpen = true,
                    doorOpenDuration = 15000L
                ),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "Critical State", showBackground = true, heightDp = 800)
@Composable
fun CriticalStatePreview() {
    MaterialTheme {
        Surface {
            FluidRefrigeratorMonitor(
                state = RefrigeratorState(
                    fridgeTemp = 8f,
                    freezerTemp = -14f,
                    topLeftDoorOpen = true,
                    topRightDoorOpen = true,
                    doorOpenDuration = 35000L
                ),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}