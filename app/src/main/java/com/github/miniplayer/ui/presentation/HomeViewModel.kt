package com.github.miniplayer.ui.presentation

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.miniplayer.R
import com.github.miniplayer.ui.model.Song
import com.github.miniplayer.ui.model.TaskStatus
import com.github.miniplayer.ui.state.UiEvent
import kotlinx.coroutines.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val musicPlayList = listOf(
        Song(
            0,
            resource = R.raw.theme,
            trackName = "Bombay"
        ),
        Song(
            1,
            resource = R.raw.alaipayuthey,
            trackName = "Alaipayuthe Kanna"
        ),
        Song(
            2,
            resource = R.raw.kadhal,
            trackName = "Kadha Sadugudu"
        ),
        Song(
            3,
            resource = R.raw.pachchai,
            trackName = "Pachai"
        ),
        Song(
            4,
            resource = R.raw.theme2,
            trackName = "Theme2"
        )
    )

    private val _itemsState = mutableStateListOf<Song>()
    val itemsState: List<Song> = _itemsState
    var lastPlayedItem: Song? = null

    init {
        _itemsState.addAll(musicPlayList)
    }

    fun uiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ItemClick -> {
                val item = event.item
                lastPlayedItem?.let {
                    if (it.index != item.index) {
                        stopSong()
                        _itemsState[it.index] = it.copy(
                            taskStatus = TaskStatus.STOPPED
                        )
                    }
                }

                when (item.taskStatus) {
                    TaskStatus.STOPPED -> {
                        _itemsState[item.index] = itemsState[item.index].copy(
                            taskStatus = TaskStatus.PLAYING
                        )
                        playSong(item.resource)
                    }
                    TaskStatus.PLAYING -> {
                        _itemsState[item.index] = itemsState[item.index].copy(
                            taskStatus = TaskStatus.PAUSED
                        )
                        pauseSong()
                    }
                    TaskStatus.PAUSED -> {
                        _itemsState[item.index] = itemsState[item.index].copy(
                            taskStatus = TaskStatus.PLAYING
                        )
                        resumeSong()
                    }
                }
                lastPlayedItem = event.item
            }
        }
    }

    lateinit var mediaPlayer: MediaPlayer

    private fun playSong(id: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
        mediaPlayer = MediaPlayer.create(getApplication(), id)
        mediaPlayer.start()
//        }
    }

    private fun pauseSong() {
//        viewModelScope.launch(Dispatchers.IO) {
        mediaPlayer.pause()
//        }
    }

    private fun resumeSong() {
//        viewModelScope.launch(Dispatchers.IO) {
        mediaPlayer.start()
//        }
    }

    private fun stopSong() {
//        viewModelScope.launch(Dispatchers.IO) {
        mediaPlayer.stop()
        mediaPlayer.release()
//        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
