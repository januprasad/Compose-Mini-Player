package com.github.miniplayer.ui.model


data class Song(
    val index: Int,
    val resource: Int,
    val trackName: String,
    var currentTime: Float = 0.0f,
    var totalDuration: Float = 0.0f,
    var taskStatus: TaskStatus = TaskStatus.STOPPED
)

enum class TaskStatus {
    STOPPED,
    PLAYING,
    PAUSED
}
