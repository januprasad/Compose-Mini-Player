package com.github.miniplayer.ui.presentation

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.miniplayer.R
import com.github.miniplayer.ui.model.Song
import com.github.miniplayer.ui.model.TaskStatus
import com.github.miniplayer.ui.state.UiEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val musicPlayList = listOf(
        Song(
            0,
            resource = R.raw.lala,
            trackName = "Lala"
        ),
        Song(
            1,
            resource = R.raw.mylife,
            trackName = "My Life"
        ),
        Song(
            2,
            resource = R.raw.stereo,
            trackName = "Stereo"
        ),
        Song(
            3,
            resource = R.raw.touch,
            trackName = "Touch"
        )
    )

    private val _itemsState = mutableStateListOf<Song>()

    //    val songState: MutableState<SongState> = mutableStateOf(SongState())
    var totalDuration: MutableState<Float> = mutableStateOf(0.0f)
    val currentTime: MutableState<Float> = mutableStateOf(0.0f)
    val itemsState: List<Song> = _itemsState
    var lastPlayedItem: Song? = null

    init {
        _itemsState.addAll(musicPlayList)
    }

    fun uiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ItemClick -> {
                val item = event.item
                lastPlayedItem?.let { oldItem ->
                    if (oldItem.resource != item.resource) {
//                        seekSong(oldItem, 0.0f, oldItem.totalDuration.toInt())
                        stopSong(oldItem)
                        stateChange(oldItem, TaskStatus.STOPPED)
                    }
                }

                when (item.taskStatus) {
                    TaskStatus.STOPPED -> {
                        stateChange(item, TaskStatus.PLAYING)
                        playSong(item)
                    }
                    TaskStatus.PLAYING -> {
                        stateChange(item, TaskStatus.PAUSED)
                        pauseSong()
                    }
                    TaskStatus.PAUSED -> {
                        stateChange(item, TaskStatus.PLAYING)
                        resumeSong(item)
                    }
                }
                lastPlayedItem = event.item
            }
        }
    }

    private fun stateChange(item: Song, state: TaskStatus) {
        val position = _itemsState.indexOfFirst { element ->
            element.resource == item.resource
        }
        _itemsState[position] = itemsState[position].copy(
            taskStatus = state
        )
    }

    private fun seekSong(item: Song, currentPosition: Float, totalDuration: Int) {
        val position = _itemsState.indexOfFirst { element ->
            element.resource == item.resource
        }
        _itemsState[position] = itemsState[position].copy(
            currentTime = currentPosition,
            totalDuration = totalDuration.toFloat()
        )
    }

    lateinit var mediaPlayer: MediaPlayer
    var playerController: Job? = null
    private fun updateProgress(song: Song) {
        mediaPlayer?.let { mediaPlayer ->
            totalDuration.value = mediaPlayer.duration.toFloat()
            val total = mediaPlayer.duration
            playerController = viewModelScope.launch {
                while (currentTime.value < total) {
                    delay(1000)
                    try {
                        currentTime.value = mediaPlayer.currentPosition.toFloat()
                        seekSong(song, currentTime.value, total)
                    } catch (e: IllegalStateException) {
                        currentTime.value = 0.toFloat()
                        seekSong(song, currentTime.value, total)
                    }
                }
            }
        }
    }

    private fun playSong(song: Song) {
//        viewModelScope.launch(Dispatchers.IO) {

        mediaPlayer = MediaPlayer.create(getApplication(), song.resource)
        mediaPlayer.start()
        updateProgress(song)
//        }
    }

    private fun pauseSong() {
//        viewModelScope.launch(Dispatchers.IO) {
        mediaPlayer.pause()
        playerController?.cancel()
//        }
    }

    private fun resumeSong(item: Song) {
//        viewModelScope.launch(Dispatchers.IO) {
        mediaPlayer.start()
        updateProgress(item)
//        }
    }

    private fun stopSong(song: Song) {
//        viewModelScope.launch(Dispatchers.IO) {
        mediaPlayer.let { mediaPlayer ->
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.release()
            seekSong(song, 0.0f, 0)
        }
        playerController?.cancel()
//        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
