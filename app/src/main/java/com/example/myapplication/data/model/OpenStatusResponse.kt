package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class OpenStatusResponse(
    @SerializedName("restaurant_id")
    val restaurantId: String,
    @SerializedName("is_currently_open")
    val isCurrentlyOpen: Boolean

)
