package com.github.miniplayer.ui.state

import com.github.miniplayer.ui.model.Song


sealed class UiEvent {
    data class ItemClick(val item: Song) : UiEvent()
    data class SeekAudio(val item: Song) : UiEvent()
}
