package com.example.myapplication.feature.restaurants.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.myapplication.R
import com.example.myapplication.domain.model.Restaurant
import com.example.myapplication.feature.restaurants.state.RestaurantsUiEvent
import com.example.myapplication.feature.restaurants.state.RestaurantsUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailsSheet(state: RestaurantsUiState, onEvent: (RestaurantsUiEvent) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

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
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 16.dp)
                            .offset(y = INFO_CARD_OFFSET)
                    )

                    // Compensates for the InfoCard offset so content below (if it would be added) is not hidden behind the card
                    Spacer(modifier = Modifier.height(INFO_CARD_OFFSET))
                }
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
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(INFO_CARD_HEIGHT)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color(0x1A000000),
                spotColor = Color(0x1A000000)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
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
        Text(
            text = filterTags.joinToString(" · "),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.semantics {
                contentDescription = "Categories: ${filterTags.joinToString(", ")}"
            }
        )
        OpenStatus(isOpen = true) // TODO: connect to /open/{id} endpoint
    }
}

@Composable
private fun OpenStatus(isOpen: Boolean) {
    val statusText = if (isOpen) "Open" else "Closed"

    Text(
        text = statusText,
        style = MaterialTheme.typography.titleLarge,
        color = if (isOpen) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        modifier = Modifier.semantics {
            contentDescription = "Restaurant is currently $statusText"
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