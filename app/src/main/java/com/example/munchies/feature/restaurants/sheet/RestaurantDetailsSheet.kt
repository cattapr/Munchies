package com.example.munchies.feature.restaurants.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.munchies.domain.model.OpenStatus
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.feature.restaurants.state.RestaurantsUiEvent
import com.example.munchies.feature.restaurants.state.RestaurantsUiState
import com.example.munchies.feature.utils.SnackbarService
import com.example.munchies.feature.utils.cardShadow
import com.example.myapplication.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailsSheet(state: RestaurantsUiState, onEvent: (RestaurantsUiEvent) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val snackbarService = remember {
        SnackbarService(coroutineScope)
    }

    LaunchedEffect(state.openStatusHasError) {
        if (state.openStatusHasError) {
            snackbarService.show(
                message = "We couldn't check if this restaurant is open right now. Please try again later.",
            )
        }
    }


    if (state.showBottomSheet && state.selectedRestaurant != null) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            onDismissRequest = {
                onEvent(RestaurantsUiEvent.OnToggleSheet)
            },
            sheetState = sheetState,
            dragHandle = null,
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxHeight()
                .semantics {
                    contentDescription = "${state.selectedRestaurant.name} details"
                }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box {
                        BannerImage(
                            imageUrl = state.selectedRestaurant.imageUrl,
                            name = state.selectedRestaurant.name,
                            onClose = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onEvent(RestaurantsUiEvent.OnToggleSheet)
                                }
                            }
                        )

                        InfoCard(
                            restaurant = state.selectedRestaurant,
                            filterTags = state.filters
                                .filter { it.id in state.selectedRestaurant.filterIds }
                                .map { it.name },
                            openStatus = state.openStatus,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(horizontal = 16.dp)
                                .offset(y = INFO_CARD_OFFSET)
                        )

                        // Compensates for the InfoCard offset so content below (if it would be added) is not hidden behind the card
                        Spacer(modifier = Modifier.height(INFO_CARD_OFFSET))
                    }
                }
                SnackbarHost(
                    hostState = snackbarService.snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun BannerImage(
    imageUrl: String,
    name: String,
    onClose: () -> Unit
) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder),
            contentDescription = "Banner image of $name",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(top = 40.dp)
                .padding(horizontal = 22.dp)
                .align(Alignment.TopStart)
                .semantics {
                    contentDescription = "Close $name details"
                    role = Role.Button
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
private fun InfoCard(
    restaurant: Restaurant,
    filterTags: List<String>,
    openStatus: OpenStatus?,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = INFO_CARD_HEIGHT)
            .cardShadow()
            .background(Color.White)
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = restaurant.name,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.semantics {
                heading()
            }
        )
        if (filterTags.isNotEmpty()) {
            Text(
                text = filterTags.joinToString(" · "),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.semantics {
                    contentDescription = "Categories: ${filterTags.joinToString(", ")}"
                }
            )
        }

        OpenStatus(openStatus = openStatus)
    }
}

@Composable
private fun OpenStatus(openStatus: OpenStatus?) {
    if (openStatus == null) return

    val text = if (openStatus.isCurrentlyOpen) "Open" else "Closed"
    val color = if (openStatus.isCurrentlyOpen)
        MaterialTheme.colorScheme.tertiary
    else
        MaterialTheme.colorScheme.error

    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        modifier = Modifier.semantics {
            contentDescription = "Restaurant is currently $text"
            liveRegion = LiveRegionMode.Polite
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun RestaurantDetailsSheetPreview() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        RestaurantDetailsSheet(RestaurantsUiState()) {}
    }
}


private val INFO_CARD_HEIGHT = 144.dp
private val INFO_CARD_OFFSET = INFO_CARD_HEIGHT / 2