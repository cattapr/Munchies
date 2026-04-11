package com.example.myapplication.domain.model

data class Restaurant(
    val id: String,
    val name: String,
    val rating: String,
    val filterIds: List<String>,
    val imageUrl: String,
    val deliveryTimeMinutes: Int
)