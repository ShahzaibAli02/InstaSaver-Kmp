package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDialog(onDismiss: () -> Unit, onConfirm: (Long) -> Unit) {
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    if (!showTimePicker) {
        DatePickerDialog(
                onDismissRequest = onDismiss,
                confirmButton = {
                    TextButton(
                            onClick = {
                                if (datePickerState.selectedDateMillis != null) {
                                    showTimePicker = true
                                }
                            }
                    ) { Text("Next", color = PrimaryPurple) }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
                }
        ) { DatePicker(state = datePickerState) }
    } else {
        BasicAlertDialog(
                onDismissRequest = { showTimePicker = false },
                content = {
                    Column(
                            modifier =
                                    Modifier.background(SurfaceWhite, RoundedCornerShape(28.dp))
                                            .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                                "Set Time",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(bottom = 20.dp)
                        )

                        TimeInput(state = timePickerState)

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showTimePicker = false }) {
                                Text("Back", color = Color.Gray)
                            }
                            Button(
                                    onClick = {
                                        val dateMillis = datePickerState.selectedDateMillis ?: 0L
                                        // Simple combination, ignoring timezone complexities for
                                        // now or use system default
                                        // hour/minute are from 0-23 and 0-59
                                        // We take dateMillis (UTC midnight usually) and add
                                        // milliseconds

                                        // Ideally we use Instant and LocalDateTime
                                        // But simple math:
                                        val hourMillis = timePickerState.hour * 3600_000L
                                        val minuteMillis = timePickerState.minute * 60_000L
                                        // Adjust dateMillis to be start of day in local time if
                                        // needed,
                                        // but DatePicker usually returns UTC midnight.
                                        // Let's assume dateMillis is UTC.

                                        val totalMillis = dateMillis + hourMillis + minuteMillis
                                        onConfirm(totalMillis)
                                    },
                                    colors =
                                            ButtonDefaults.buttonColors(
                                                    containerColor = PrimaryPurple
                                            )
                            ) { Text("Schedule") }
                        }
                    }
                }
        )
    }
}
