package com.example.munchies.feature.restaurants.state

import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.Restaurant

sealed class RestaurantsContentState {
    object Loading : RestaurantsContentState()
    data class Success(
        val restaurants: List<Restaurant>,
        val allRestaurants: List<Restaurant>,
        val filters: List<Filter>
    ) : RestaurantsContentState()
    object Error : RestaurantsContentState()
}