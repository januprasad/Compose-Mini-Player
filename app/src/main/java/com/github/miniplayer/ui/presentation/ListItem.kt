package com.github.miniplayer.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.miniplayer.ui.model.Song
import com.github.miniplayer.ui.model.TaskStatus


@Composable
fun PlayerButtons(
    item: Song,
    background: Color,
    onItemClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(background)
            .clickable {
                onItemClick(item.resource)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            text = item.trackName,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Box(
            modifier = Modifier.size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            /** show indicator only for loading state */
            when (item.taskStatus) {
                TaskStatus.PLAYING -> {
                    Icon(
                        painterResource(id = com.github.miniplayer.R.drawable.ic_pause),
                        "action icon",
                        tint = Color.Red
                    )
                }
                TaskStatus.PAUSED -> {
                    Icon(
                        painterResource(id = com.github.miniplayer.R.drawable.ic_play),
                        "action icon",
                        tint = Color.Green
                    )
                }
                TaskStatus.STOPPED -> {
                    Icon(
                        painterResource(id = com.github.miniplayer.R.drawable.ic_play),
                        "action icon",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}
