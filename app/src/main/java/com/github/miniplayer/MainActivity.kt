package com.github.miniplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.miniplayer.ui.presentation.HomeScreen
import com.github.miniplayer.ui.theme.ParallelProcessingCoroutinesTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParallelProcessingCoroutinesTheme {
                HomeScreen()
            }
        }
    }
}