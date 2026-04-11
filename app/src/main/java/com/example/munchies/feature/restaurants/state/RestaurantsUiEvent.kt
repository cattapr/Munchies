package com.example.munchies.feature.restaurants.state

import com.example.munchies.domain.model.Restaurant

sealed class RestaurantsUiEvent {
    data object OnRefresh : RestaurantsUiEvent()
    data object OnToggleSheet : RestaurantsUiEvent()
    data class OnFilterSelected(val filterId: String) : RestaurantsUiEvent()
    data class OnRestaurantSelected(val restaurant: Restaurant) : RestaurantsUiEvent()
}