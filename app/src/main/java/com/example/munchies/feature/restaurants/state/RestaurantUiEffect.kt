package com.example.munchies.feature.restaurants.state

sealed class RestaurantsUiEffect {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null
    ) : RestaurantsUiEffect()

    data class ShowSheetSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val restaurantId: String
    ) : RestaurantsUiEffect()
}