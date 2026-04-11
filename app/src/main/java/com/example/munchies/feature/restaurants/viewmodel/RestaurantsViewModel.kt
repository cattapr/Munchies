package com.example.munchies.feature.restaurants.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.domain.usecases.IRestaurantsUseCases
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent
import com.example.munchies.feature.restaurants.state.RestaurantsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel
@Inject
constructor(
    private val restaurantsUseCases: IRestaurantsUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(RestaurantsUiState())
    val state = _state.asStateFlow()

    init {
        loadRestaurants()
    }

    fun onEvent(event: RestaurantsUiEvent) {
        when (event) {
            RestaurantsUiEvent.OnRefresh -> {
                loadRestaurants(isRefreshing = true)
            }

            RestaurantsUiEvent.OnToggleSheet -> {
                handleToggleSheet()
            }

            is RestaurantsUiEvent.OnFilterSelected -> {
                handleOnFilterSelected(selectedId = event.filterId)
            }

            is RestaurantsUiEvent.OnRestaurantSelected -> {
                handleOnRestaurantSelected(event.restaurant)
            }
        }
    }


    private fun loadRestaurants(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if (isRefreshing) {
                _state.update { it.copy(isRefreshing = true, hasError = false) }
            } else {
                _state.update { it.copy(isLoading = true, hasError = false) }
            }

            restaurantsUseCases.getAllRestaurants().fold(
                onSuccess = { restaurants ->
                    _state.update {
                        it.copy(
                            restaurants = restaurants,
                            allRestaurants = restaurants,
                            hasError = false,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    loadFilters(restaurants)
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            hasError = true,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            )
        }
    }

    private fun loadFilters(restaurants: List<Restaurant>) {
        viewModelScope.launch {
            // Collect unique filter IDs across all restaurants
            val uniqueFilterIds = restaurants
                .flatMap { it.filterIds }
                .toSet()

            // Fetch each unique filter
            val filters = uniqueFilterIds.mapNotNull { filterId ->
                restaurantsUseCases.getFilter(filterId).getOrNull()
            }

            _state.update { it.copy(filters = filters, isLoading = false) }
        }
    }

    private fun handleOnFilterSelected(selectedId: String) {
        viewModelScope.launch {
            val currentSelected = _state.value.selectedFilterIds

            // Toggle — add if not selected, remove if already selected
            val newSelectedIds = if (selectedId in currentSelected) {
                currentSelected - selectedId
            } else {
                currentSelected + selectedId
            }

            val filteredRestaurants = if (newSelectedIds.isEmpty()) {
                _state.value.allRestaurants
            } else {
                _state.value.allRestaurants.filter { restaurant ->
                    newSelectedIds.all { it in restaurant.filterIds }
                }
            }

            _state.update {
                it.copy(
                    selectedFilterIds = newSelectedIds,
                    restaurants = filteredRestaurants
                )
            }
        }
    }

    private fun handleOnRestaurantSelected(restaurant: Restaurant) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedRestaurant = restaurant,
                    showBottomSheet = true,
                    openStatus = null
                )
            }
            loadOpenStatus(restaurant.id)
        }
    }

    private fun loadOpenStatus(restaurantId: String) {
        viewModelScope.launch {
            _state.update { it.copy(openStatusHasError = false) }

            restaurantsUseCases.getOpenStatus(restaurantId).fold(
                onSuccess = { openStatus ->
                    _state.update { it.copy(openStatus = openStatus) }
                },
                onFailure = {
                    _state.update { it.copy(openStatusHasError = true) }
                }
            )
        }
    }

    private fun handleToggleSheet() {
        viewModelScope.launch {
            _state.update { it.copy(showBottomSheet = !it.showBottomSheet) }
        }
    }

}