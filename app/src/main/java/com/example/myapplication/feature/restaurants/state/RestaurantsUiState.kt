package com.example.myapplication.feature.restaurants.state

import com.example.myapplication.domain.model.Filter
import com.example.myapplication.domain.model.Restaurant


data class RestaurantsUiState(
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val restaurants: List<Restaurant> = emptyList(),
    val allRestaurants: List<Restaurant> = emptyList(),
    val filters: List<Filter> = emptyList(),
    val selectedFilterIds: Set<String> = emptySet()
)