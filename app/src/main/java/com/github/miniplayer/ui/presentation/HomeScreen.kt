package com.github.miniplayer.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.miniplayer.ui.model.Song
import com.github.miniplayer.ui.model.TaskStatus
import com.github.miniplayer.ui.state.UiEvent

@Composable
fun HomeScreen(state: List<Song>, uiEvent: (UiEvent) -> Unit) {
    /**
     * chat audio messages filter to make a list
     */

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
            PlayerButtons(
                item = item,
                background = Color.White,
                onItemClick = {
                    uiEvent(UiEvent.ItemClick(item))
                }
            )
            PlayerSlider(item = item) {
                uiEvent(UiEvent.SeekAudio(it))
            }
        }
    }
}

@Composable
fun PlayerSlider(item: Song, seekAudio: (Song) -> Unit) {
    Column(
        modifier = Modifier
            .width(198.dp)
            .padding(start = 20.dp)
    ) {
        Slider(
            value = item.currentTime.toFloat(),
            onValueChange = {
                if (item.taskStatus == TaskStatus.PLAYING) {
                    val song = item.copy(
                        currentTime = it
                    )
                    seekAudio(song)
                }
            },
            valueRange = 0f..item.totalDuration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Red,
                activeTrackColor = Color.Green
            )
        )
    }
}
