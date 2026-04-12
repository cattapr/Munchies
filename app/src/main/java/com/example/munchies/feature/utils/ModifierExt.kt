package com.example.munchies.feature.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.munchies.feature.theme.DropShadow

fun Modifier.dropShadow(
    shadows: List<DropShadow>,
    shape: Shape = RoundedCornerShape(12.dp)
): Modifier = this.drawBehind {
    shadows.forEach { shadow ->
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                asFrameworkPaint().apply {
                    isAntiAlias = true
                    color = android.graphics.Color.TRANSPARENT
                    setShadowLayer(
                        shadow.blur.toPx(),
                        shadow.offset.x.toPx(),
                        shadow.offset.y.toPx(),
                        shadow.color.toArgb()
                    )
                }
            }
            canvas.drawRoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                radiusX = when (shape) {
                    is RoundedCornerShape -> shape.topStart.toPx(size, this)
                    else -> 0f
                },
                radiusY = when (shape) {
                    is RoundedCornerShape -> shape.topStart.toPx(size, this)
                    else -> 0f
                },
                paint = paint
            )
        }
    }
}