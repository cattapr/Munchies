package com.example.myapplication.ui.delivery.state

import com.example.myapplication.domain.model.Restaurant


data class DeliveryUiState(
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val restaurants: List<Restaurant> = emptyList()
)