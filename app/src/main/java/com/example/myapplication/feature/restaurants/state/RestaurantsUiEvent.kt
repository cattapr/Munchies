package com.example.myapplication.feature.restaurants.state

sealed class RestaurantsUiEvent {
    data object OnToggleSheet : RestaurantsUiEvent()
    data class OnFilterSelected(val filterId: String) : RestaurantsUiEvent()
}