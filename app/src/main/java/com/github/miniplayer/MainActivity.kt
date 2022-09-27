package com.github.miniplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.miniplayer.ui.presentation.HomeScreen
import com.github.miniplayer.ui.presentation.HomeViewModel
import com.github.miniplayer.ui.theme.MiniPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiniPlayerTheme {
                HomeScreen(homeViewModel.itemsState){
                    homeViewModel.uiEvent(it)
                }
            }
        }
    }
}