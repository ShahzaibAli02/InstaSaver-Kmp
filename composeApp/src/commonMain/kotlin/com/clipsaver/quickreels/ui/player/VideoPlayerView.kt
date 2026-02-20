package com.clipsaver.quickreels.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun VideoPlayerView(
    videoPath: String,
    modifier: Modifier = Modifier
)