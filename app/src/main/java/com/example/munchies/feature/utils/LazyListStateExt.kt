package com.example.munchies.feature.utils

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState

suspend fun LazyListState.smoothScrollToIndex(index: Int) {
    val visibleItems = layoutInfo.visibleItemsInfo
    val clickedItem = visibleItems.firstOrNull { it.index == index } ?: return

    val viewportStart = layoutInfo.viewportStartOffset
    val viewportEnd = layoutInfo.viewportEndOffset
    val itemStart = clickedItem.offset
    val itemEnd = clickedItem.offset + clickedItem.size

    val scrollAmount = when {
        // Item is cut off on the right → scroll right
        itemEnd > viewportEnd -> (itemEnd - viewportEnd + clickedItem.size).toFloat()
        // Item is cut off on the left → scroll left
        itemStart < viewportStart -> -(viewportStart - itemStart + clickedItem.size).toFloat()
        else -> return  // fully visible, no scroll needed
    }

    animateScrollBy(
        value = scrollAmount,
        animationSpec = tween(
            durationMillis = 600,
            easing = EaseInOutCubic
        )
    )
}