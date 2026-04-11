package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.ui.delivery.DeliveryView
import com.example.myapplication.ui.delivery.state.DeliveryUiState
import com.example.myapplication.ui.delivery.viewmodel.DeliveryViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val deliveryViewModel: DeliveryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var deliveryUiState: DeliveryUiState by mutableStateOf(DeliveryUiState())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                deliveryViewModel.state.collectLatest { deliveryUiState = it }
            }
        }

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DeliveryView(
                    deliveryUiState,
                    deliveryViewModel::onEvent
                )
            }
        }
    }
}