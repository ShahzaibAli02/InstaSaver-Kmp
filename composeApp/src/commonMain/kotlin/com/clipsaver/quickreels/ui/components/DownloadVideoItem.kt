package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.theme.DarkTextCharcoal
import com.clipsaver.quickreels.ui.theme.LightTextGray
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite

@Composable fun DownloadVideoItem(
    video: Video,
    onVideoClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
    onCancelDownload: () -> Unit,
    onRetryClick: () -> Unit,
)
{
    var expanded by remember { mutableStateOf(false) }

    Card(
            modifier = Modifier.fillMaxWidth().clickable(
                    enabled = (video.isDownloaded && video.videoPath.isNotEmpty()),
                    onClick = onVideoClick
            ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column { // Thumbnail Container
            Box(
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(1.3f) // Adjust aspect ratio as needed
                        .background(Color.LightGray)
            ) {
                AsyncImage(
                        model = video.thumbnailPath,
                        contentDescription = video.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                )

                if (video.isDownloading)
                { // Downloading Overlay
                    Box(
                            modifier = Modifier.fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                                progress = { video.progress },
                                modifier = Modifier.size(48.dp),
                                color = PrimaryPurple,
                                trackColor = Color.White.copy(alpha = 0.3f),
                        )
                        Text(
                                text = "${(video.progress * 100).toInt()}%",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else if (video.isFailed)
                { // Failed Overlay
                    Box(
                            modifier = Modifier.fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Error,
                                    contentDescription = "Failed",
                                    tint = Color.Red,
                                    modifier = Modifier.size(32.dp)
                            )
                            Text(
                                    text = "Failed",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else
                { // Play Button Overlay
                    Box(
                            modifier = Modifier.align(Alignment.Center).size(48.dp).background(
                                        Color.Black.copy(alpha = 0.5f),
                                        CircleShape
                                ),
                            contentAlignment = Alignment.Center
                    ) {
                        Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                        )
                    }

//                    Box(
//                            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).background(
//                                    LightTextGray.copy(alpha = 0.8f),
//                                        RoundedCornerShape(4.dp)
//                                ).padding(
//                                        horizontal = 6.dp,
//                                        vertical = 2.dp
//                                )
//
//                    ) {
//                        Icon(
//                                imageVector = Icons.Default.MoreHoriz,
//                                contentDescription = "More",
//                                tint = PrimaryPurple,
//                                modifier = Modifier.size(24.dp).clickable { expanded = true })
//
//                        DropdownMenu(
//                                expanded = expanded,
//                                onDismissRequest = { expanded = false },
//                                modifier = Modifier.background(SurfaceWhite)
//                        ) {
//                            if (video.isDownloading)
//                            {
//                                DropdownMenuItem(
//                                        text = { Text("Cancel") },
//                                        onClick = {
//                                            expanded = false
//                                            onCancelDownload()
//                                        },
//                                        leadingIcon = {
//                                            Icon(
//                                                    imageVector = Icons.Default.Cancel,
//                                                    contentDescription = "Cancel",
//                                                    tint = PrimaryPurple
//                                            )
//                                        })
//                            } else if (video.isFailed)
//                            {
//                                DropdownMenuItem(
//                                        text = { Text("Retry") },
//                                        onClick = {
//                                            expanded = false
//                                            onRetryClick()
//                                        },
//                                        leadingIcon = {
//                                            Icon(
//                                                    imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
//                                                    contentDescription = "Retry",
//                                                    tint = PrimaryPurple
//                                            )
//                                        })
//                                DropdownMenuItem(
//                                        text = { Text("Delete") },
//                                        onClick = {
//                                            expanded = false
//                                            onDeleteClick()
//                                        },
//                                        leadingIcon = {
//                                            Icon(
//                                                    imageVector = Icons.Default.Delete,
//                                                    contentDescription = "Delete",
//                                                    tint = PrimaryPurple
//                                            )
//                                        })
//                            } else
//                            {
//                                DropdownMenuItem(
//                                        text = { Text("Repost") },
//                                        onClick = {
//                                            expanded = false
//                                            onShareClick()
//                                        },
//                                        leadingIcon = {
//                                            Icon(
//                                                    imageVector = Icons.Default.Share,
//                                                    contentDescription = "Share",
//                                                    tint = PrimaryPurple
//                                            )
//                                        })
//                                DropdownMenuItem(
//                                        text = { Text("Delete") },
//                                        onClick = {
//                                            expanded = false
//                                            onDeleteClick()
//                                        },
//                                        leadingIcon = {
//                                            Icon(
//                                                    imageVector = Icons.Default.Delete,
//                                                    contentDescription = "Delete",
//                                                    tint = PrimaryPurple
//                                            )
//                                        })
//                            }
//                        }
//                    }


                //                    // Quality Badge
                    //                    Box(
                    //                            modifier =
                    //                                Modifier.align(Alignment.TopEnd)
                    //                                    .padding(8.dp)
                    //                                    .background(PrimaryPurple, RoundedCornerShape(4.dp))
                    //                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    //                    ) {
                    //                        Text(
                    //                                text = video.quality,
                    //                                color = Color.White,
                    //                                fontSize = 10.sp,
                    //                                fontWeight = FontWeight.Bold
                    //                        )
                    //                    }
                }
            }

//             Info Section
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                        text = video.name,
                                        style =
                                            MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                            ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = DarkTextCharcoal,
                                        modifier = Modifier.weight(1f)
                                )

                                // More Options (Delete, etc.)
                                Box {
                                    Icon(
                                            imageVector = Icons.Default.MoreHoriz,
                                            contentDescription = "More",
                                            tint = PrimaryPurple,
                                            modifier = Modifier.size(24.dp).clickable { expanded = true }
                                    )

                                    DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            modifier = Modifier.background(SurfaceWhite)
                                    ) {
                                        if (video.isDownloading) {
                                            DropdownMenuItem(
                                                    text = { Text("Cancel") },
                                                    onClick = {
                                                        expanded = false
                                                        onCancelDownload()
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                                imageVector = Icons.Default.Cancel,
                                                                contentDescription = "Cancel",
                                                                tint = PrimaryPurple
                                                        )
                                                    }
                                            )
                                        } else if (video.isFailed) {
                                            DropdownMenuItem(
                                                    text = { Text("Retry") },
                                                    onClick = {
                                                        expanded = false
                                                        onRetryClick()
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                                imageVector =
                                                                    androidx.compose.material.icons.Icons
                                                                        .Default.Refresh,
                                                                contentDescription = "Retry",
                                                                tint = PrimaryPurple
                                                        )
                                                    }
                                            )
                                            DropdownMenuItem(
                                                    text = { Text("Delete") },
                                                    onClick = {
                                                        expanded = false
                                                        onDeleteClick()
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                                imageVector = Icons.Default.Delete,
                                                                contentDescription = "Delete",
                                                                tint = PrimaryPurple
                                                        )
                                                    }
                                            )
                                        } else {
                                            DropdownMenuItem(
                                                    text = { Text(Strings.share) },
                                                    onClick = {
                                                        expanded = false
                                                        onShareClick()
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                                imageVector = Icons.Default.Share,
                                                                contentDescription = "Share",
                                                                tint = PrimaryPurple
                                                        )
                                                    }
                                            )
                                            DropdownMenuItem(
                                                    text = { Text(Strings.delete) },
                                                    onClick = {
                                                        expanded = false
                                                        onDeleteClick()
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                                imageVector = Icons.Default.Delete,
                                                                contentDescription = "Delete",
                                                                tint = PrimaryPurple
                                                        )
                                                    }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                    text = video.duration,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LightTextGray
                            )
                        }
        }
    }
}
