package com.example.munchies.domain.model

data class OpenStatus(
    val restaurantId: String,
    val isCurrentlyOpen: Boolean,
)