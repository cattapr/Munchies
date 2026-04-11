package com.example.munchies.feature.restaurants.state

import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.model.Restaurant


data class RestaurantsUiState(
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val restaurants: List<Restaurant> = emptyList(),
    val allRestaurants: List<Restaurant> = emptyList(),
    val filters: List<Filter> = emptyList(),
    val selectedFilterIds: Set<String> = emptySet(),
    val selectedRestaurant: Restaurant? = null,
    val openStatus: OpenStatus? = null
)