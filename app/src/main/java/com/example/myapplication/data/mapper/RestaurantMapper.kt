package com.example.myapplication.data.mapper

import com.example.myapplication.data.model.ApiRestaurant
import com.example.myapplication.data.model.RestaurantsResponse
import com.example.myapplication.domain.model.Restaurant

fun RestaurantsResponse.toUiRestaurant(): List<Restaurant> =
    restaurants.map { it.toDomain() }

fun ApiRestaurant.toDomain() = Restaurant(
    id = id,
    name = name,
    rating = rating,
    filterIds = filterIds,
    imageUrl = imageUrl,
    deliveryTimeMinutes = deliveryTimeMinutes
)