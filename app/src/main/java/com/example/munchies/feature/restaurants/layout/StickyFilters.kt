package com.example.munchies.feature.restaurants.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.munchies.domain.model.Filter
import com.example.munchies.feature.restaurants.components.FilterButton
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent
import com.example.munchies.feature.utils.smoothScrollToIndex
import kotlinx.coroutines.launch


@Composable
fun StickyFilters(
    filters: List<Filter>,
    selectedFilterIds: Set<String>,
    onEvent: (RestaurantsUiEvent) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 22.dp)
            .semantics {
                contentDescription = "Filter restaurants by category"
            }
    ) {
        itemsIndexed(filters) { index, filter ->
            FilterButton(
                filter = filter,
                isSelected = filter.id in selectedFilterIds,
                onClick = {
                    onEvent(RestaurantsUiEvent.OnFilterSelected(filter.id))
                    coroutineScope.launch {
                        listState.smoothScrollToIndex(index)
                    }
                }
            )
        }
    }
}