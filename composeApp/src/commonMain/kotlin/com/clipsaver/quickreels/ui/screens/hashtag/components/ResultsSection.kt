package com.clipsaver.quickreels.ui.screens.hashtag.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clipsaver.quickreels.ui.theme.PrimaryColor
import com.clipsaver.quickreels.ui.theme.Slate200

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ResultsSection(
        hashtags: List<String>,
        onClear: () -> Unit,
        onCopyAll: () -> Unit,
        onShuffle: () -> Unit
) {
    Column {
        Row(
                modifier =
                        Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                        text = "Generated Results",
                        style =
                                TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A) // Slate-900
                                )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                        modifier =
                                Modifier.clip(RoundedCornerShape(50))
                                        .background(PrimaryColor.copy(alpha = 0.1f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                            text = "${hashtags.size}/30",
                            style =
                                    TextStyle(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryColor
                                    )
                    )
                }
            }

            IconButton(
                    onClick = onShuffle,
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.Transparent)
            ) {
                Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = Color(0xFF64748B), // Slate-500
                        modifier = Modifier.size(20.dp)
                )
            }
        }

        Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Slate200)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    hashtags.forEach { tag ->
                        Text(
                                text = tag,
                                style =
                                        TextStyle(
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = PrimaryColor,
                                                lineHeight = 24.sp
                                        ),
                                modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                // Dotted/Solid Line Separator
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0xFFF1F5F9)) // Slate-100
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    // Start of Clear Button
                    Button(
                            onClick = onClear,
                            colors =
                                    ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFF1F5F9), // Slate-100
                                            contentColor = Color(0xFF475569) // Slate-600
                                    ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier.height(32.dp)
                    ) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                                text = "Clear",
                                style =
                                        TextStyle(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                        )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    // Copy All Button
                    Button(
                            onClick = onCopyAll,
                            colors =
                                    ButtonDefaults.buttonColors(
                                            containerColor = PrimaryColor.copy(alpha = 0.1f),
                                            contentColor = PrimaryColor
                                    ),
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                                text = "Copy All",
                                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
