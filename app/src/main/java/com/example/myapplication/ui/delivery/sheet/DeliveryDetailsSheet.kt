package com.example.myapplication.ui.delivery.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.delivery.DeliveryUiEvent
import com.example.myapplication.ui.delivery.state.DeliveryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryDetailsSheet(state: DeliveryUiState, onEvent: (DeliveryUiEvent) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (state.showBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White,
            contentColor = Color.Black,
            onDismissRequest = { onEvent(DeliveryUiEvent.OnToggleSheet) },
            sheetState = sheetState
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Stickers",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeliveryDetailsSheetPreview() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        DeliveryDetailsSheet(DeliveryUiState()) {}
    }
}