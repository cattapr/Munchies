package com.example.munchies.feature.restaurants.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.domain.usecases.IRestaurantsUseCases
import com.example.munchies.feature.restaurants.state.RestaurantsContentState
import com.example.munchies.feature.restaurants.state.RestaurantsUiEffect
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent
import com.example.munchies.feature.restaurants.state.RestaurantsUiState
import com.example.munchies.feature.restaurants.state.SelectedRestaurantState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _effect = MutableSharedFlow<RestaurantsUiEffect>()
    val effect = _effect.asSharedFlow()


    init {
        loadRestaurants()
    }

    fun onEvent(event: RestaurantsUiEvent) {
        when (event) {
            RestaurantsUiEvent.OnRefresh -> {
                loadRestaurants(isRefreshing = true)
            }

            RestaurantsUiEvent.OnRetry -> {
                loadRestaurants()
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

            is RestaurantsUiEvent.OnRetryLoadOpenStatus -> {
                loadOpenStatus(event.restaurantId, ignoreCache = true)
            }
        }
    }


    private fun loadRestaurants(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRefreshing = isRefreshing,
                    contentState = if (isRefreshing) it.contentState else RestaurantsContentState.Loading
                )
            }

            restaurantsUseCases.getAllRestaurants(ignoreCache = isRefreshing).fold(
                onSuccess = { restaurants ->
                    loadFilters(restaurants)
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            contentState = if (isRefreshing) it.contentState else RestaurantsContentState.Error
                        )
                    }
                    viewModelScope.launch {
                        _effect.emit(
                            RestaurantsUiEffect.ShowSnackbar(
                                message = "Something went wrong, please try again",
                                actionLabel = "Retry"
                            )
                        )
                    }
                }
            )
        }
    }

    private fun loadFilters(restaurants: List<Restaurant>) {
        viewModelScope.launch {
            val uniqueFilterIds = restaurants
                .flatMap { it.filterIds }
                .toSet()

            val filters = uniqueFilterIds.mapNotNull { filterId ->
                restaurantsUseCases.getFilter(filterId).getOrNull()
            }

            _state.update {
                it.copy(
                    isRefreshing = false,
                    contentState = RestaurantsContentState.Success(
                        restaurants = restaurants,
                        allRestaurants = restaurants,
                        filters = filters
                    )
                )
            }
        }
    }

    private fun handleOnFilterSelected(selectedId: String) {
        viewModelScope.launch {
            val currentState = _state.value.contentState
            if (currentState !is RestaurantsContentState.Success) return@launch

            val newSelectedIds = if (selectedId in _state.value.selectedFilterIds) {
                _state.value.selectedFilterIds - selectedId
            } else {
                _state.value.selectedFilterIds + selectedId
            }

            val filteredRestaurants = if (newSelectedIds.isEmpty()) {
                currentState.allRestaurants
            } else {
                currentState.allRestaurants.filter { restaurant ->
                    newSelectedIds.all { it in restaurant.filterIds }
                }
            }

            _state.update {
                it.copy(
                    selectedFilterIds = newSelectedIds,
                    contentState = currentState.copy(restaurants = filteredRestaurants)
                )
            }
        }
    }

    private fun handleOnRestaurantSelected(restaurant: Restaurant) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showBottomSheet = true,
                    selectedRestaurantState = SelectedRestaurantState(restaurant = restaurant)
                )
            }
            loadOpenStatus(restaurant.id)
        }
    }

    private fun loadOpenStatus(restaurantId: String, ignoreCache: Boolean = false) {
        viewModelScope.launch {
            restaurantsUseCases.getOpenStatus(restaurantId, ignoreCache).fold(
                onSuccess = { openStatus ->
                    _state.update {
                        it.copy(
                            selectedRestaurantState = it.selectedRestaurantState?.copy(
                                openStatus = openStatus,
                                openStatusHasError = false
                            )
                        )
                    }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            selectedRestaurantState = it.selectedRestaurantState?.copy(
                                openStatus = null,
                                openStatusHasError = true
                            )
                        )
                    }

                    viewModelScope.launch {
                        _effect.emit(
                            RestaurantsUiEffect.ShowSheetSnackbar(
                                message = "We couldn't check if this restaurant is open right now. Please try again later.",
                                actionLabel = "Retry",
                                restaurantId = restaurantId
                            )
                        )
                    }
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