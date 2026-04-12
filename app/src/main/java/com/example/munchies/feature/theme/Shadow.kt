package com.example.munchies.feature.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

data class DropShadow(
    val color: Color,
    val offset: DpOffset,
    val blur: Dp,
    val spread: Dp = 0.dp
)

object MunchiesShadow {
    // Filter button shadow - 4% opacity, blur 10
    val filterButton: List<DropShadow> = listOf(
        DropShadow(
            color = Color(red = 0, green = 0, blue = 0, alpha = 10), // 4%
            offset = DpOffset(x = 0.dp, y = 4.dp),
            blur = 10.dp
        )
    )

    // Card shadow - 10% opacity, blur 4
    val card: List<DropShadow> = listOf(
        DropShadow(
            color = Color(red = 0, green = 0, blue = 0, alpha = 26), // 10%
            offset = DpOffset(x = 0.dp, y = 4.dp),
            blur = 4.dp
        )
    )
}