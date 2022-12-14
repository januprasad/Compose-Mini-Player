package com.github.miniplayer.ui.presentation

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.miniplayer.musicPlayList
import com.github.miniplayer.ui.model.Song
import com.github.miniplayer.ui.model.TaskStatus
import com.github.miniplayer.ui.state.UiEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    /**
     * listSongs observe composable.
     */
    private val _itemsState = mutableStateListOf<Song>()
    val itemsState: List<Song> = _itemsState

    var totalDuration: MutableState<Float> = mutableStateOf(0.0f)
    val currentTime: MutableState<Float> = mutableStateOf(0.0f)

    var lastPlayedItem: Song? = null

    //    val songState: MutableState<SongState> = mutableStateOf(SongState())
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

            is UiEvent.SeekAudio -> {
                seekAudio(event.item)
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
        mediaPlayer.let { mediaPlayer ->
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
            mediaPlayer.reset()
            mediaPlayer.release()
            seekSong(song, 0.0f, 0)
        }
        playerController?.cancel()
//        }
    }

    fun seekAudio(song: Song) {
//        lastPlayedItem?.let { oldItem ->
//            if (oldItem.resource == song.resource) {
        updateMediaPlayer(song.currentTime)
//            }
//        }
    }

    private fun updateMediaPlayer(position: Float) {
        mediaPlayer.let { mediaPlayer ->
            viewModelScope.launch {
                mediaPlayer.seekTo(position.toInt())
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}
