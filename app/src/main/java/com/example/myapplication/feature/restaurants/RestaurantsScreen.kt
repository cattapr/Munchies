package com.example.myapplication.feature.restaurants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.domain.model.Restaurant
import com.example.myapplication.feature.restaurants.components.RestaurantCard
import com.example.myapplication.feature.restaurants.sheet.RestaurantDetailsSheet
import com.example.myapplication.feature.restaurants.state.RestaurantsUiEvent
import com.example.myapplication.feature.restaurants.state.RestaurantsUiState

@Composable
fun RestaurantsScreen(state: RestaurantsUiState, onEvent: (RestaurantsUiEvent) -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            RestaurantsTopBar()
        }
    ) { innerPadding ->
        RestaurantList(
            restaurants = state.restaurants,
            modifier = Modifier.padding(innerPadding)
        )
        RestaurantDetailsSheet(state, onEvent)
    }
}

@Composable
private fun RestaurantList(
    restaurants: List<Restaurant>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(restaurants) { restaurant ->
            RestaurantCard(restaurant)
        }
    }
}

@Composable
private fun RestaurantsTopBar() {
    Column {
        Logo()
        StickyFilters()
    }
}

@Composable
private fun StickyFilters() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        // TODO: filters goes here
    }
}

@Composable
private fun Logo() {
    Icon(
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = "Logo",
        modifier = Modifier
            .padding(all = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun RestaurantsScreenPreview() {
    RestaurantsScreen(state = RestaurantsUiState()) {}
}