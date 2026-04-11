package com.example.myapplication.feature.restaurants.state

sealed class RestaurantsUiEvent {
    data object OnToggleSheet : RestaurantsUiEvent()
}