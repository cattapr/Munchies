package com.example.myapplication.ui.delivery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.delivery.sheet.DeliveryDetailsSheet
import com.example.myapplication.ui.delivery.state.DeliveryUiState

@Composable
fun DeliveryView(state: DeliveryUiState, onEvent: (DeliveryUiEvent) -> Unit) {
    Scaffold(
        containerColor = Color.White,
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // TODO: Filters
            // TODO: Restaurant cards
            state.restaurants.forEach {
                Text(it.name)
            }
        }


        DeliveryDetailsSheet(state, onEvent)
    }
}


@Preview(showBackground = true)
@Composable
private fun DeliveryViewPreview() {
    DeliveryView(state = DeliveryUiState()) {}
}