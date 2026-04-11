package com.example.myapplication.feature.restaurants.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.IRestaurantsUseCases
import com.example.myapplication.feature.restaurants.state.RestaurantsUiEvent
import com.example.myapplication.feature.restaurants.state.RestaurantsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.fold

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
        }
    }

    private fun loadRestaurants() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            restaurantsUseCases.getAllRestaurants().fold(
                onSuccess = { restaurants ->
                    _state.update { it.copy(restaurants = restaurants, hasError = false) }
                },
                onFailure = {
                    _state.update { it.copy(hasError = true) }
                }
            )
            _state.update { it.copy(isLoading = false) }
        }
    }
}