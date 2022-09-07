package com.github.miniplayer.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
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
                it.index
            }
        ) { item ->
            ListItem(
                item = item,
                background = Color.White,
                onItemClick = {
                    viewModel.uiEvent(UiEvent.ItemClick(item))
                }
            )
        }
    }
}
