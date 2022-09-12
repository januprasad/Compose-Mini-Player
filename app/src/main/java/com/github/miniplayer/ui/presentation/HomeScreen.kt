package com.github.miniplayer.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.miniplayer.ui.model.Song
import com.github.miniplayer.ui.state.UiEvent

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state = viewModel.itemsState

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top
    ) {
        items(
            items = state,
            key = {
                it.resource
            }
        ) { item ->
            ListItem(
                item = item,
                background = Color.White,
                onItemClick = {
                    viewModel.uiEvent(UiEvent.ItemClick(item))
                }
            )
            PlayerSlider(item = item)
        }
    }
}

@Composable
fun PlayerSlider(item: Song) {
    Column(
        modifier = Modifier
            .width(198.dp)
            .padding(start = 20.dp)
    ) {
        Slider(
            value = item.currentTime.toFloat(),
            onValueChange = {},
            valueRange = 0f..item.totalDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Green
            )
        )
    }
}
