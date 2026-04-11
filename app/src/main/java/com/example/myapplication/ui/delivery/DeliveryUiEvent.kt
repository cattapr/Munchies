package com.example.myapplication.ui.delivery

sealed class DeliveryUiEvent {
    data object OnToggleSheet : DeliveryUiEvent()
}