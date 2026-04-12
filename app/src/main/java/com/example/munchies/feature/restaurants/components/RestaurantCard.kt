package com.example.munchies.feature.restaurants.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.munchies.domain.model.Restaurant
import com.example.munchies.feature.theme.ClockIconColor
import com.example.munchies.feature.theme.RatingTextColor
import com.example.munchies.feature.theme.StarIconColor
import com.example.munchies.feature.utils.cardShadow
import com.example.myapplication.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RestaurantCard(
    modifier: Modifier = Modifier,
    restaurant: Restaurant,
    filterTags: List<String>,
    onRestaurantClick: (Restaurant) -> Unit
) {
    val cardShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "cardPressScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .then(
                if (!pressed) Modifier.cardShadow(shape = cardShape)
                else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onRestaurantClick(restaurant) }
            )
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = "${restaurant.name}, " +
                        "rated ${restaurant.rating}, " +
                        "delivery time ${restaurant.deliveryTimeMinutes} minutes, " +
                        "categories: ${filterTags.joinToString(", ")}"
                onClick(
                    label = "See more details",
                    action = null
                )
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { invisibleToUser() },
            shape = cardShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                RestaurantImage(imageUrl = restaurant.imageUrl, name = restaurant.name)
                RestaurantInfo(restaurant = restaurant, filterTags)
            }
        }
    }
}

@Composable
private fun RestaurantImage(imageUrl: String, name: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(false)
            .build(),
        placeholder = painterResource(R.drawable.img_placeholder),
        error = painterResource(R.drawable.img_placeholder),
        contentDescription = "Image of $name",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
    )
}

@Composable
private fun RestaurantInfo(restaurant: Restaurant, filterTags: List<String>) {
    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        RestaurantTitleRow(restaurant = restaurant)
        RestaurantTags(filterTags)
        RestaurantDeliveryTime(deliveryTimeMinutes = restaurant.deliveryTimeMinutes)
    }
}

@Composable
private fun RestaurantTitleRow(restaurant: Restaurant) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = restaurant.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        RestaurantRating(rating = restaurant.rating)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RestaurantRating(rating: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.semantics(mergeDescendants = true) {
            contentDescription = "Rating: $rating out of 5"
        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = null,
            tint = StarIconColor,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = rating,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = RatingTextColor,
            modifier = Modifier.semantics {
                invisibleToUser()
            },
        )
    }
}

@Composable
private fun RestaurantTags(filterTags: List<String>) {
    Text(
        text = filterTags.joinToString(" · "),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.semantics {
            contentDescription = "Categories: ${filterTags.joinToString(", ")}"
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RestaurantDeliveryTime(deliveryTimeMinutes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.semantics(mergeDescendants = true) {
            contentDescription = "Delivery time: $deliveryTimeMinutes minutes"
        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = null,
            tint = ClockIconColor,
            modifier = Modifier.size(10.dp)
        )
        Text(
            text = "$deliveryTimeMinutes mins",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.semantics {
                invisibleToUser()
            },
        )
    }
}