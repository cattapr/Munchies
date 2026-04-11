package com.example.myapplication.feature.restaurants.state

import com.example.myapplication.domain.model.Restaurant

sealed class RestaurantsUiEvent {
    data object OnToggleSheet : RestaurantsUiEvent()
    data class OnFilterSelected(val filterId: String) : RestaurantsUiEvent()
    data class OnRestaurantSelected(val restaurant: Restaurant) : RestaurantsUiEvent()
}