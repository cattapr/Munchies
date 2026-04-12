package com.example.munchies.feature.restaurants.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.munchies.domain.model.Filter
import com.example.munchies.feature.theme.MunchiesShadow
import com.example.munchies.feature.utils.dropShadow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilterButton(
    filter: Filter,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.White
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground

    val shape = RoundedCornerShape(24.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .height(48.dp)
            .dropShadow(
                shadows = MunchiesShadow.filterButton,
                shape = shape
            )
            .clip(shape)
            .background(backgroundColor)
            .clickable(
                onClick = onClick,
                onClickLabel = if (isSelected) "Deselect ${filter.name}" else "Select ${filter.name}"
            )
            .semantics(mergeDescendants = true) {
                contentDescription = filter.name
                selected = isSelected
                role = Role.Button
            }
            .padding(end = 16.dp)
    ) {
        AsyncImage(
            model = filter.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .clip(CircleShape)
        )
        Text(
            text = filter.name,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            modifier = Modifier.semantics {
                hideFromAccessibility()
            },
        )
    }
}