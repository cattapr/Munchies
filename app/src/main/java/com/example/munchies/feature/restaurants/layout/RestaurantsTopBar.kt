package com.example.munchies.feature.restaurants.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.munchies.domain.model.Filter
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent
import com.example.myapplication.R

@Composable
fun RestaurantsTopBar(
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
private fun Logo() {
    Icon(
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = "Umain logo",
        modifier = Modifier
            .padding(all = 16.dp)
    )
}
