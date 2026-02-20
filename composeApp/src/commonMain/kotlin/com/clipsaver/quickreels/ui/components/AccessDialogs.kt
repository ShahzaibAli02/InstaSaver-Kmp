package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.clipsaver.quickreels.ui.theme.PrimaryPurple

@Composable
fun UpdateRequiredDialog(title: String, message: String, onUpdate: () -> Unit) {
    AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                )
            },
            text = { Text(text = message, fontSize = 16.sp, textAlign = TextAlign.Center) },
            confirmButton = {
                Button(
                        onClick = onUpdate,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier =
                                Modifier.fillMaxSize()
                                        .background(
                                                brush =
                                                        Brush.horizontalGradient(
                                                                colors =
                                                                        listOf(
                                                                                Color(
                                                                                        0xFFFF69B4
                                                                                ), // Pink-ish
                                                                                PrimaryPurple // Purple
                                                                        )
                                                        ),
                                                shape =
                                                        ByteArray(50) { 1 }.let {
                                                            RoundedCornerShape(50)
                                                        } // Circle/Stadium
                                        )
                ) { Text("Update Now", color = Color.White) }
            },
            properties =
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            containerColor = Color.White,
            tonalElevation = 0.dp
    )
    // Note: The specific styling in the image (gradient button, centered text, rounded dialog)
    // might require a custom Dialog instead of AlertDialog if AlertDialog is too restrictive.
    // For now, attempting with AlertDialog but custom implementation is cleaner for exact match.
}

@Composable
fun CustomGradientButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = ButtonDefaults.ContentPadding,
            modifier =
                    modifier.background(
                            brush =
                                    Brush.horizontalGradient(
                                            colors = listOf(Color(0xFFff4d9e), Color(0xFFff007f))
                                    ),
                            shape = RoundedCornerShape(12.dp)
                    )
    ) { Text(text = text, color = Color.White, fontWeight = FontWeight.Bold) }
}

// Re-implementing with Basic Dialog for better control to match screenshots
@Composable
fun BaseAccessDialog(
        title: String,
        message: String,
        confirmText: String,
        onConfirm: () -> Unit,
        dismissText: String? = null,
        onDismiss: (() -> Unit)? = null,
        cancellable: Boolean = false
) {
    Dialog(
            onDismissRequest = { if (cancellable && onDismiss != null) onDismiss() },
            properties =
                    DialogProperties(
                            dismissOnBackPress = cancellable,
                            dismissOnClickOutside = cancellable
                    )
    ) {
        Box(
                modifier =
                        Modifier.background(Color.White, shape = RoundedCornerShape(24.dp))
                                .padding(24.dp),
                contentAlignment = Alignment.Center
        ) {
            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                        text = title,
                        style =
                                MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp
                                ),
                        textAlign = TextAlign.Center,
                        color = Color.Black
                )

                Text(
                        text = message,
                        style =
                                MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                ),
                        textAlign = TextAlign.Center
                )

                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (dismissText != null && onDismiss != null && cancellable) {
                        OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFFF69B4)) // Pink border
                        ) {
                            Text(
                                    dismissText,
                                    color = Color(0xFFC2185B),
                                    fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors =
                                    ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                                modifier =
                                        Modifier.fillMaxSize()
                                                .background(
                                                        brush =
                                                                Brush.horizontalGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        Color(
                                                                                                0xFFFF4081
                                                                                        ),
                                                                                        Color(
                                                                                                0xFFF50057
                                                                                        )
                                                                                )
                                                                ),
                                                        shape = RoundedCornerShape(12.dp)
                                                ),
                                contentAlignment = Alignment.Center
                        ) { Text(confirmText, color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}
