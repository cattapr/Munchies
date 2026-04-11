package com.example.myapplication.ui.delivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.delivery.DeliveryUiEvent
import com.example.myapplication.ui.delivery.state.DeliveryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel
@Inject
constructor() : ViewModel() {
    private val _state = MutableStateFlow(DeliveryUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: DeliveryUiEvent) {
        when (event) {
            DeliveryUiEvent.OnToggleSheet -> {
                viewModelScope.launch {
                    _state.update { it.copy(showBottomSheet = !it.showBottomSheet) }
                }
            }
        }
    }
}