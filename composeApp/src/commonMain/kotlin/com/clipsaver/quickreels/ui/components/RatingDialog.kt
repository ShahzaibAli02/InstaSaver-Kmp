package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite

@Composable
fun RatingDialog(onDismiss: () -> Unit, onRate: () -> Unit) {
    var rating by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.padding(16.dp)
        ) {
            Box {
                // Close Button
//                IconButton(
//                        onClick = onDismiss,
//                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
//                ) { Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray) }

                Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                            text = "Please Rate Us ?",
                            style =
                                    MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                    ),
                            textAlign = TextAlign.Center,
                            color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                            text =
                                    "Your review will help us improve our application to help your problem!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Stars
                    Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(5) { index ->
                            val isSelected = index < rating
                            Icon(
                                    imageVector =
                                            if (isSelected) Icons.Default.Star
                                            else Icons.Outlined.Star,
                                    contentDescription = null,
                                    tint =
                                            if (isSelected) Color(0xFFFFD700)
                                            else Color.LightGray, // Gold vs Gray
                                    modifier =
                                            Modifier.size(40.dp)
                                                    .clickable { rating = index + 1 }
                                                    .padding(4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                            onClick = onRate,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) { Text("Submit", color = Color.White, fontWeight = FontWeight.Bold) }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = onDismiss) { Text("No, Thanks!", color = Color.Gray) }
                }
            }
        }
    }
}
