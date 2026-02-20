package com.clipsaver.quickreels.ui.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import coil3.compose.AsyncImage
import com.clipsaver.quickreels.domain.models.ScheduledPost
import com.clipsaver.quickreels.presentation.viewmodels.ScheduleViewModel
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.components.EditScheduleDialog
import com.clipsaver.quickreels.ui.components.HeaderTitle
import com.clipsaver.quickreels.ui.theme.BackgroundOffWhite
import com.clipsaver.quickreels.ui.theme.DarkTextCharcoal
import com.clipsaver.quickreels.ui.theme.LightTextGray
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime // import kotlinx.datetime.Instant
import org.koin.compose.viewmodel.koinViewModel

// import kotlin.time.Instant

@Composable fun ScheduleScreen(viewModel: ScheduleViewModel = koinViewModel())
{
    val posts by viewModel.scheduledPosts.collectAsState()
    var postToEdit by remember { mutableStateOf<ScheduledPost?>(null) }
    val colorScheme = MaterialTheme.colorScheme
    if (postToEdit != null)
    {
        EditScheduleDialog(
                post = postToEdit!!,
                onDismiss = { postToEdit = null },
                onConfirm = { time, desc, tags ->
                    viewModel.updatePost(
                            postToEdit!!.copy(
                                    scheduledTime = time,
                                    description = desc,
                                    hashtags = tags
                            )
                    )
                    postToEdit = null
                })
    }

    Scaffold(
            containerColor = colorScheme.background, topBar = {
        HeaderTitle(title = Strings.Schedule)
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) { // Header
            Spacer(modifier = Modifier.height(10.dp))

            // Posts List
            if (posts.isEmpty())
            {
                ScheduleEmptyState()
            } else
            {
                ScheduleHeader(
                        postsCount = posts.size,
                        colorScheme = colorScheme
                )

                Spacer(modifier = Modifier.height(20.dp))

                ScheduleList(
                        posts = posts,
                        onDelete = { id -> viewModel.deletePost(id) },
                        onEdit = { post -> postToEdit = post })
            }
        }
    }
}

@OptIn(ExperimentalTime::class) @Composable fun ScheduledPostItem(
    post: ScheduledPost,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
)
{
    var showMenu by remember { mutableStateOf(false) }

    Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                    containerColor = Color.White,
            )
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            val now = Clock.System.now().toEpochMilliseconds()
            val isFuture = post.scheduledTime > now
            val displayStatus = when
            {
                post.status == "DRAFT" -> "DRAFT"
                isFuture -> "QUEUE"
                else -> "READY"
            }

            // Thumbnail with Status Icon (SAME AS BEFORE)
            Box(modifier = Modifier.size(80.dp)) {
                AsyncImage(
                        model = post.thumbnailPath,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp))
                )

                // Status Indicator
                val (statusIcon, statusColor) = when (displayStatus)
                {
                    "READY" -> Icons.Default.Check to Color(0xFF4ADE80) // Green
                    "DRAFT" -> Icons.Default.Edit to Color(0xFFFACC15) // Yellow
                    else -> Icons.Default.Schedule to Color(0xFF9CA3AF) // Gray
                }

                Box(
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(16.dp)
                            .background(
                                    statusColor,
                                    CircleShape
                            ).border(
                                    1.dp,
                                    SurfaceWhite,
                                    CircleShape
                            ),
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                    )
                }
            }

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = formatTime(post.scheduledTime),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextCharcoal
                            )
                    )

                    // Status Badge
                    val (badgeBg, badgeText, badgeTextColor) = when (displayStatus)
                    {
                        "READY" -> Triple(
                                Color(0xFFDCFCE7),
                                "Ready",
                                Color(0xFF16A34A)
                        )

                        "DRAFT" -> Triple(
                                Color(0xFFFEF9C3),
                                "Draft",
                                Color(0xFFCA8A04)
                        )

                        else -> Triple(
                                Color(0xFFF3F4F6),
                                "Queue",
                                Color(0xFF4B5563)
                        )
                    }

                    Text(
                            text = badgeText.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = badgeTextColor
                            ),
                            modifier = Modifier.background(
                                    badgeBg,
                                    CircleShape
                            ).padding(
                                    horizontal = 8.dp,
                                    vertical = 2.dp
                            )
                    )
                }

                Text(
                        text = post.description.ifBlank { "No description" },
                        style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                        text = post.hashtags,
                        style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = PrimaryPurple.copy(alpha = 0.7f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                )
            }

            // More Options
            Box {
                IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = LightTextGray
                    )
                }
                DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                showMenu = false
                                onEdit()
                            },
                            leadingIcon = {
                                Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null
                                )
                            })
                    DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null
                                )
                            })
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class) fun formatTime(epochMillis: Long): String
{
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val dateTime = instant.toLocalDateTime(
            TimeZone.currentSystemDefault()
    ) // 12-hour format manually since KMP doesn't have // java.time.format.DateTimeFormatter easily
    val hour = dateTime.hour
    val minute = dateTime.minute
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    return "${hour12}:${
        minute.toString().padStart(
                2,
                '0'
        )
    } $amPm"
}

@Composable private fun ScheduleHeader(
    postsCount: Int,
    colorScheme: androidx.compose.material3.ColorScheme,
)
{
    Row(
            modifier = Modifier.fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
                text = "Upcoming Posts",
                style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                )
        )
        Text(
                text = "$postsCount Scheduled",
                style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.primary
                ),
                modifier = Modifier.background(
                        colorScheme.primary.copy(alpha = 0.1f),
                        RoundedCornerShape(6.dp)
                ).padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                )
        )
    }
}

@Composable private fun ScheduleEmptyState()
{
    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = LightTextGray,
                    modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                    text = "No Scheduled Posts",
                    style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkTextCharcoal
                    )
            )
            Text(
                    text = "Your upcoming scheduled posts will appear here.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                            color = LightTextGray
                    ),
                    modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable private fun ScheduleList(
    posts: List<ScheduledPost>,
    onDelete: (String) -> Unit,
    onEdit: (ScheduledPost) -> Unit,
)
{
    LazyColumn(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->
            ScheduledPostItem(
                    post = post,
                    onDelete = { onDelete(post.id) },
                    onEdit = { onEdit(post) })
        }
    }
}
