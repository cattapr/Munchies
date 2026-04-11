package com.example.munchies

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.munchies.feature.restaurants.RestaurantsScreen
import com.example.munchies.feature.restaurants.state.RestaurantsUiState
import com.example.munchies.feature.restaurants.viewmodel.RestaurantsViewModel
import com.example.munchies.feature.theme.MunchiesApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val deliveryViewModel: RestaurantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var deliveryUiState: RestaurantsUiState by mutableStateOf(RestaurantsUiState())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                deliveryViewModel.state.collectLatest { deliveryUiState = it }
            }
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )

        setContent {
            MunchiesApplicationTheme {
                RestaurantsScreen(
                    deliveryUiState,
                    deliveryViewModel::onEvent
                )
            }
        }
    }
}