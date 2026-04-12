package com.example.munchies.feature.restaurants.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.feature.restaurants.components.RestaurantCard
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent

@Composable
fun RestaurantList(
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
        item {
            Spacer(modifier.height(16.dp))
        }
    }
}
