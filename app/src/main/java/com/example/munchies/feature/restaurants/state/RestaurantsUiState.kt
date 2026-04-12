package com.example.munchies.feature.restaurants.state

import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.model.Restaurant

data class RestaurantsUiState(
    val showBottomSheet: Boolean = false,
    val isRefreshing: Boolean = false,
    val selectedFilterIds: Set<String> = emptySet(),
    val contentState: RestaurantsContentState = RestaurantsContentState.Loading,
    val selectedRestaurantState: SelectedRestaurantState? = null
)

data class SelectedRestaurantState(
    val restaurant: Restaurant,
    val openStatus: OpenStatus? = null,
    val openStatusHasError: Boolean = false
)