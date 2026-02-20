package com.clipsaver.quickreels.ui.screens.hashtag.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clipsaver.quickreels.ui.theme.PrimaryColor
import com.clipsaver.quickreels.ui.theme.Slate200

@Composable
fun HistoryItem(tags: String, onCopy: () -> Unit, onDelete: () -> Unit) {
    Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)), // Slate-50
            border = BorderStroke(1.dp, Slate200),
            modifier = Modifier.fillMaxWidth()
    ) {
        Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                    text = tags,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style =
                            TextStyle(
                                    fontSize = 14.sp,
                                    color = Color(0xFF334155), // Slate-700
                                    lineHeight = 20.sp
                            ),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
            )

            Row {
                IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                    Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = PrimaryColor,
                            modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFEF4444), // Red-500
                            modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
