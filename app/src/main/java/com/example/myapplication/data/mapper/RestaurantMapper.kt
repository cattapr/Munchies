package com.example.myapplication.data.mapper

import com.example.myapplication.data.model.ApiRestaurant
import com.example.myapplication.data.model.FilterResponse
import com.example.myapplication.data.model.RestaurantsResponse
import com.example.myapplication.domain.model.Filter
import com.example.myapplication.domain.model.Restaurant

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