package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantsResponse(
    val restaurants: List<ApiRestaurant>
)

data class ApiRestaurant(
    val id: String,
    val name: String,
    val rating: Float,
    val filterIds: List<String>,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("delivery_time_minutes")
    val deliveryTimeMinutes: Int
)