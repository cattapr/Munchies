package com.example.munchies.feature.restaurants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.feature.restaurants.components.FilterButton
import com.example.munchies.feature.restaurants.components.RestaurantCard
import com.example.munchies.feature.restaurants.sheet.RestaurantDetailsSheet
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent
import com.example.munchies.feature.restaurants.state.RestaurantsUiState

@Composable
fun RestaurantsScreen(state: RestaurantsUiState, onEvent: (RestaurantsUiEvent) -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            RestaurantsTopBar(
                filters = state.filters,
                selectedFilterIds = state.selectedFilterIds,
                onEvent = onEvent
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                AnimatedVisibility(
                    visible = state.restaurants.isNotEmpty(),
                    enter = fadeIn() + slideInVertically()
                ) {
                    RestaurantList(
                        restaurants = state.restaurants,
                        filters = state.filters,
                        onEvent = onEvent
                    )
                }
            }
        }
        RestaurantDetailsSheet(state, onEvent)
    }
}

@Composable
private fun RestaurantList(
    restaurants: List<Restaurant>,
    modifier: Modifier = Modifier,
    filters: List<Filter>,
    onEvent: (RestaurantsUiEvent) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .semantics {
                contentDescription = "${restaurants.size} restaurants available"
            },
    ) {
        items(restaurants) { restaurant ->
            val filterTags = filters
                .filter { it.id in restaurant.filterIds }
                .map { it.name }

            RestaurantCard(
                restaurant = restaurant,
                filterTags = filterTags,
                onRestaurantClick = { onEvent(RestaurantsUiEvent.OnRestaurantSelected(restaurant)) }
            )
        }
    }
}

@Composable
private fun RestaurantsTopBar(
    filters: List<Filter>,
    selectedFilterIds: Set<String>,
    onEvent: (RestaurantsUiEvent) -> Unit
) {
    Column(modifier = Modifier.statusBarsPadding()) {
        Logo()
        StickyFilters(
            filters = filters,
            selectedFilterIds = selectedFilterIds,
            onEvent = onEvent
        )
    }
}

@Composable
private fun StickyFilters(
    filters: List<Filter>,
    selectedFilterIds: Set<String>,
    onEvent: (RestaurantsUiEvent) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 22.dp)
            .semantics {
                contentDescription = "Filter restaurants"
            }
    ) {
        items(filters) { filter ->
            FilterButton(
                filter = filter,
                isSelected = filter.id in selectedFilterIds,
                onClick = { onEvent(RestaurantsUiEvent.OnFilterSelected(filter.id)) }
            )
        }
    }
}

@Composable
private fun Logo() {
    Icon(
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = "Umain logo",
        modifier = Modifier
            .padding(all = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun RestaurantsScreenPreview() {
    RestaurantsScreen(state = RestaurantsUiState()) {}
}