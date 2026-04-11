package com.example.myapplication.feature.restaurants.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Restaurant
import com.example.myapplication.domain.usecases.IRestaurantsUseCases
import com.example.myapplication.feature.restaurants.state.RestaurantsUiEvent
import com.example.myapplication.feature.restaurants.state.RestaurantsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel
@Inject
constructor(private val restaurantsUseCases: IRestaurantsUseCases) : ViewModel() {
    private val _state = MutableStateFlow(RestaurantsUiState())
    val state = _state.asStateFlow()

    init {
        loadRestaurants()
    }

    fun onEvent(event: RestaurantsUiEvent) {
        when (event) {
            RestaurantsUiEvent.OnToggleSheet -> {
                viewModelScope.launch {
                    _state.update { it.copy(showBottomSheet = !it.showBottomSheet) }
                }
            }

            is RestaurantsUiEvent.OnFilterSelected -> {
                handleOnFilterSelected(selectedId = event.filterId)
            }

            is RestaurantsUiEvent.OnRestaurantSelected -> {
                _state.update {
                    it.copy(
                        selectedRestaurant = event.restaurant,
                        showBottomSheet = true
                    )
                }
            }
        }
    }

    private fun loadRestaurants() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            restaurantsUseCases.getAllRestaurants().fold(
                onSuccess = { restaurants ->
                    _state.update {
                        it.copy(
                            restaurants = restaurants,
                            allRestaurants = restaurants,
                            hasError = false
                        )
                    }
                    loadFilters(restaurants)
                },
                onFailure = {
                    _state.update { it.copy(hasError = true, isLoading = false) }
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
}