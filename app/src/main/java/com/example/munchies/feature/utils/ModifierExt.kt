package com.example.munchies.feature.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.cardShadow(
    elevation: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(12.dp)
): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    ambientColor = Color.Black.copy(alpha = 0.3f),
    spotColor = Color.Black.copy(alpha = 0.3f)
)