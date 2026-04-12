package com.example.munchies.feature.restaurants

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.munchies.domain.model.Filter
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.feature.restaurants.components.FilterButton
import com.example.munchies.feature.restaurants.components.RestaurantCard
import com.example.munchies.feature.restaurants.sheet.RestaurantDetailsSheet
import com.example.munchies.feature.restaurants.state.RestaurantsContentState
import com.example.munchies.feature.restaurants.state.RestaurantsUiEffect
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent
import com.example.munchies.feature.restaurants.state.RestaurantsUiState
import com.example.munchies.feature.utils.SnackbarService
import com.example.munchies.feature.utils.smoothScrollToIndex
import com.example.myapplication.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RestaurantsScreen(
    state: RestaurantsUiState,
    effect: SharedFlow<RestaurantsUiEffect>,
    onEvent: (RestaurantsUiEvent) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarService = remember {
        SnackbarService(coroutineScope)
    }

    CollectEffects(effect = effect, snackbarService = snackbarService, onEvent = onEvent)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            RestaurantsTopBar(
                filters = if (state.contentState is RestaurantsContentState.Success) {
                    state.contentState.filters
                } else emptyList(),
                selectedFilterIds = state.selectedFilterIds,
                onEvent = onEvent
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarService.snackbarHostState)
        },
    ) { innerPadding ->
        RestaurantsContent(
            state = state,
            effect = effect,
            onEvent = onEvent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun CollectEffects(
    effect: SharedFlow<RestaurantsUiEffect>,
    snackbarService: SnackbarService,
    onEvent: (RestaurantsUiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        effect.collect { effect ->
            when (effect) {
                is RestaurantsUiEffect.ShowSnackbar -> {
                    snackbarService.show(
                        message = effect.message,
                        actionLabel = effect.actionLabel,
                        onAction = { onEvent(RestaurantsUiEvent.OnRetry) }
                    )
                }

                else -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestaurantsContent(
    state: RestaurantsUiState,
    effect: SharedFlow<RestaurantsUiEffect>,
    onEvent: (RestaurantsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onEvent(RestaurantsUiEvent.OnRefresh) },
        state = pullToRefreshState,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = state.isRefreshing,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .semantics { invisibleToUser() }
            )
        },
        modifier = modifier
            .fillMaxSize()
            .semantics {
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = "Refresh restaurants",
                        action = {
                            onEvent(RestaurantsUiEvent.OnRefresh)
                            true
                        }
                    )
                )
            }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            when (val contentState = state.contentState) {
                is RestaurantsContentState.Loading -> LoadingContent()
                is RestaurantsContentState.Success -> SuccessContent(
                    contentState = contentState,
                    onEvent = onEvent
                )

                is RestaurantsContentState.Error -> {
                    // Error is handled by snackbar
                }
            }
        }
        RestaurantDetailsSheet(state, effect, onEvent)
    }
}

@Composable
private fun BoxScope.LoadingContent() {
    CircularProgressIndicator(
        modifier = Modifier
            .align(Alignment.Center)
            .semantics { contentDescription = "Loading restaurants" },
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun SuccessContent(
    contentState: RestaurantsContentState.Success,
    onEvent: (RestaurantsUiEvent) -> Unit
) {
    AnimatedVisibility(
        visible = contentState.restaurants.isNotEmpty(),
        enter = fadeIn() + slideInVertically()
    ) {
        RestaurantList(
            restaurants = contentState.restaurants,
            filters = contentState.filters,
            onEvent = onEvent
        )
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
        item {
            Spacer(modifier.height(16.dp))
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
    RestaurantsScreen(state = RestaurantsUiState(), effect = MutableSharedFlow()) {}
}