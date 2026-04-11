package com.example.myapplication.domain.model

data class Restaurant(
    val id: String,
    val name: String,
    val rating: Float,
    val filterIds: List<String>,
    val imageUrl: String,
    val deliveryTimeMinutes: Int
)