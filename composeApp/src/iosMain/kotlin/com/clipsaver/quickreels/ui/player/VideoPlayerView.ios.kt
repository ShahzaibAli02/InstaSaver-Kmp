package com.clipsaver.quickreels.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.play
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL

import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive

import platform.AVFoundation.*


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayerView(
    videoPath: String,
    modifier: Modifier
) {
    // 1. Create the Player and Controller once and remember them
    val player = remember { AVPlayer() }
    val playerViewController = remember { AVPlayerViewController() }

    // 2. Configure the Audio Session (Best to do this once)
    LaunchedEffect(Unit) {
        val audioSession = AVAudioSession.sharedInstance()
        try {
            audioSession.setCategory(AVAudioSessionCategoryPlayback, error = null)
            audioSession.setActive(true, error = null)
        } catch (e: Exception) {
            println("Error setting up audio session: ${e.message}")
        }
    }

    // 3. Handle Video Path Changes & Cleanup
    DisposableEffect(videoPath) {
        // Determine if it's a remote URL or local file
        val url = if (videoPath.startsWith("http")) {
            NSURL.URLWithString(videoPath)
        } else {
            NSURL.fileURLWithPath(videoPath)
        }

        // Create player item and start playing
        val playerItem = url?.let { AVPlayerItem(uRL = it) }
        player.replaceCurrentItemWithPlayerItem(playerItem)
        player.play()

        // Cleanup when this Composable is removed from the screen
        onDispose {
            player.pause()
            player.replaceCurrentItemWithPlayerItem(null)
        }
    }

    // 4. Render the View
    UIKitView(
            modifier = modifier,
            factory = {
                playerViewController.player = player
                playerViewController.showsPlaybackControls = true

                // Return the controller's view to Compose
                playerViewController.view
            },
            update = {
                // If you need to update layout properties, do it here.
                // Note: We handle video loading in DisposableEffect, not here.
            }
    )
}
