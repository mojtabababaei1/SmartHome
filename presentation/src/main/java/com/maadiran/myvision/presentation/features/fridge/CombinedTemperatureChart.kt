package com.maadiran.myvision.presentation.features.fridge

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun CombinedTemperatureChart(
    fridgeTemps: List<Float>,
    freezeTemps: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val maxPoints = maxOf(fridgeTemps.size, freezeTemps.size)
        if (maxPoints < 2) return@Canvas

        // بازه ثابت دما
        val minTemp = -30f
        val maxTemp = 30f
        val tempRange = maxTemp - minTemp

        fun List<Float>.normalize(): List<Float> =
            map { 1f - ((it - minTemp) / tempRange).coerceIn(0f, 1f) }

        val fridgeY = fridgeTemps.normalize()
        val freezeY = freezeTemps.normalize()

        fun createSmoothPath(yPoints: List<Float>): Path {
            val path = Path()
            val stepX = width / (yPoints.size - 1)
            path.moveTo(0f, yPoints[0] * height)
            for (i in 1 until yPoints.size) {
                val x1 = (i - 1) * stepX
                val y1 = yPoints[i - 1] * height
                val x2 = i * stepX
                val y2 = yPoints[i] * height
                val midX = (x1 + x2) / 2
                val midY = (y1 + y2) / 2
                path.quadraticBezierTo(x1, y1, midX, midY)
            }
            return path
        }

        // رسم منحنی‌ها
        drawPath(createSmoothPath(fridgeY), Color.Blue, style = Stroke(4f))
        drawPath(createSmoothPath(freezeY), Color.Cyan, style = Stroke(4f))

        // خطوط راهنما و برچسب دما
        val labelCount = 7 // -30, -20, -10, 0, +10, +20, +30
        val labelStep = tempRange / (labelCount - 1)

        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.DKGRAY
            textSize = 26f
            isAntiAlias = true
        }

        drawIntoCanvas { canvas ->
            for (i in 0 until labelCount) {
                val tempLabel = minTemp + i * labelStep
                val yPos = height - (i * (height / (labelCount - 1)))

                // خط افقی
//                drawLine(
//                    color = Color.LightGray,
//                    start = Offset(0f, yPos),
//                    end = Offset(width, yPos),
//                    strokeWidth = 1f
//                )

                // برچسب دما
                canvas.nativeCanvas.drawText(
                    String.format("%.0f°C", tempLabel),
                    0f,
                    yPos - 4,
                    textPaint
                )
            }

            // برچسب محور X (زمان بر حسب ثانیه)
            val intervalSeconds = 5
            val xLabelCount = 5
            val stepX = width / (xLabelCount - 1)

            for (i in 0 until xLabelCount) {
                val idx = i * (maxPoints - 1) / (xLabelCount - 1)
                val timeLabel = (maxPoints - 1 - idx) * intervalSeconds

                canvas.nativeCanvas.drawText(
                    "${timeLabel}s",
                    i * stepX,
                    height + 20f, // پایین‌تر برای جلوگیری از تداخل با دما
                    textPaint
                )

            }
        }
    }
}
