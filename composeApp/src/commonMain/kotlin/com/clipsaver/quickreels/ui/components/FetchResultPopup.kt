package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.clipsaver.quickreels.data.remote.models.Media
import com.clipsaver.quickreels.data.remote.models.VideoResponse
import com.clipsaver.quickreels.ui.screens.home.QualityChip
import com.clipsaver.quickreels.ui.theme.AccentPinkStart
import com.clipsaver.quickreels.ui.theme.DarkTextCharcoal
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite
import com.clipsaver.quickreels.utils.Util

@Composable
fun FetchResultPopup(
        videoResponse: VideoResponse,
        onDismiss: () -> Unit,
        onDownload: (Media) -> Unit,
        isPremium: Boolean,
        onLockedItemClick: () -> Unit
) {
    var selectedMedia: Media? by remember { mutableStateOf(videoResponse.getMedia().firstOrNull()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                            text = "Fetch Result",
                            style =
                                    MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                    ),
                            modifier = Modifier.align(Alignment.Center),
                            color = DarkTextCharcoal
                    )
                    IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                                modifier = Modifier.size(40.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = AccentPinkStart
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Thumbnail
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.LightGray)
                ) {
                    AsyncImage(
                            model = videoResponse.thumbnail,
                            contentDescription = "Thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                    )

                    // Play Button Overlay
                    Box(
                            modifier =
                                    Modifier.align(Alignment.Center)
                                            .size(48.dp)
                                            .background(PrimaryPurple, CircleShape),
                            contentAlignment = Alignment.Center
                    ) {
                        Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                        )
                    }

                    // Duration
                    if (videoResponse.duration != null) {
                        Box(
                                modifier =
                                        Modifier.align(Alignment.BottomStart)
                                                .padding(8.dp)
                                                .background(
                                                        Color.Black.copy(alpha = 0.6f),
                                                        RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                    text =
                                            Util.formatDuration(
                                                    videoResponse.duration
                                            ), // Helper function needed
                                    color = Color.White,
                                    fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title & Info
                Text(
                        text = videoResponse.title ?: "Unknown Title",
                        style =
                                MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = DarkTextCharcoal,
                        modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quality Selection (Mock for now, or based on medias)
                Text(
                        text = "Quality ",
                        style =
                                MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold
                                ),
                        color = DarkTextCharcoal,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(videoResponse.getMedia()) { index, media ->
                        val isLocked = !isPremium && index > 0
                        QualityChip(
                                modifier =
                                        Modifier.clickable {
                                            if (isLocked) {
                                                onLockedItemClick()
                                            } else {
                                                selectedMedia = media
                                            }
                                        },
                                quality = media.quality.orEmpty(),
                                type = media.type,
                                selected = selectedMedia == media,
                                locked = isLocked
                        )
                    }
                }
                //                Row(horizontalArrangement =
                // Arrangement.spacedBy(8.dp)) {
                //                     QualityChip(quality = "1080p", selected =
                // true)
                //                     QualityChip(quality = "720p", selected =
                // false)
                //                     QualityChip(quality = "480p", selected =
                // false)
                //                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                        onClick = { selectedMedia?.let { media -> onDownload(media) } },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(8.dp),
                        enabled = selectedMedia != null
                ) { Text("Download Video", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    }
}
