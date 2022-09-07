package com.github.miniplayer.ui.model


data class Song(
    val index: Int,
    val resource: Int,
    val trackName: String,
    var taskStatus: TaskStatus = TaskStatus.STOPPED
)

enum class TaskStatus {
    STOPPED,
    PLAYING,
    PAUSED
}
