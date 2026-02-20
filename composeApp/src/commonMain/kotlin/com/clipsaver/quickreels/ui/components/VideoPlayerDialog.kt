package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.clipsaver.quickreels.ads.Ads
import com.clipsaver.quickreels.domain.models.ScheduledPost
import com.clipsaver.quickreels.presentation.viewmodels.ScheduleViewModel
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.player.VideoPlayerView
import org.koin.compose.koinInject //import kotlinx.datetime.Clock
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class) @Composable
fun VideoPlayerDialog(
    videoPath: String,
    thumbnailPath: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onShareClick: () -> Unit,
) {
    var showScheduleDialog by remember { mutableStateOf(false) }

    if (showScheduleDialog) {
        ScheduleDialog(
                onDismiss = { showScheduleDialog = false },
                onConfirm = { timestamp ->
                    val id = "scheduled_${Clock.System.now().toEpochMilliseconds()}"
                    val post = ScheduledPost(
                                    id = id,
                                    videoPath = videoPath,
                                    thumbnailPath = thumbnailPath,
                                    scheduledTime = timestamp,
                                    description = "",
                                    hashtags = "",
                                    status = "READY",
                                    isPosted = false
                            )
                    showScheduleDialog = false
                    onDismiss() // Dismiss player after scheduling
                }
        )
    }

    Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Box(modifier = modifier.weight(1f)) {
                VideoPlayerView(videoPath = videoPath, modifier = Modifier.fillMaxSize())
                IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
//                Button(onClick = { showScheduleDialog = true }, modifier = Modifier.weight(1f)) {
//                    Text("Schedule")
//                }
                Button(onClick = {

                    onShareClick()

                }, modifier = Modifier.weight(1f)) { Text(Strings.share) }
            }
        }
    }
}
