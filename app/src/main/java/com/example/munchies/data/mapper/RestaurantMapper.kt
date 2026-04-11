package com.example.munchies.data.mapper

import com.example.munchies.data.model.ApiRestaurant
import com.example.munchies.data.model.FilterResponse
import com.example.munchies.data.model.OpenStatusResponse
import com.example.munchies.data.model.RestaurantsResponse
import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.model.Restaurant

fun RestaurantsResponse.toUiRestaurant(): List<Restaurant> =
    restaurants.map { it.toRestaurant() }

fun ApiRestaurant.toRestaurant() = Restaurant(
    id = id,
    name = name,
    rating = rating.toString(),
    filterIds = filterIds,
    imageUrl = imageUrl,
    deliveryTimeMinutes = deliveryTimeMinutes
)

fun FilterResponse.toUiFilter() = Filter(
    id = id,
    name = name,
    imageUrl = imageUrl
)

fun OpenStatusResponse.toUiOpenStatus() = OpenStatus(
    restaurantId = restaurantId,
    isCurrentlyOpen = isCurrentlyOpen
)