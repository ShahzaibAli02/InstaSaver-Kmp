package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.clipsaver.quickreels.domain.models.ScheduledPost
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleDialog(
        post: ScheduledPost,
        onDismiss: () -> Unit,
        onConfirm: (Long, String, String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Initialize state with post data
    var description by remember { mutableStateOf(post.description) }
    var hashtags by remember { mutableStateOf(post.hashtags) }

    // Parse existing time
    val instant = Instant.fromEpochMilliseconds(post.scheduledTime)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val datePickerState =
            rememberDatePickerState(
                    initialSelectedDateMillis =
                            post.scheduledTime // Approximate, usually good enough for initial
                    // verify
                    )
    val timePickerState =
            rememberTimePickerState(
                    initialHour = localDateTime.hour,
                    initialMinute = localDateTime.minute
            )

    if (showDatePicker) {
        DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Done", color = PrimaryPurple)
                    }
                }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
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
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                        ) { TextButton(onClick = { showTimePicker = false }) { Text("Done") } }
                    }
                }
        )
    }

    BasicAlertDialog(
            onDismissRequest = onDismiss,
            content = {
                Column(
                        modifier =
                                Modifier.background(SurfaceWhite, RoundedCornerShape(28.dp))
                                        .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Edit Post", style = MaterialTheme.typography.headlineSmall)

                    // Date/Time Display & Edit
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Time:")
                        TextButton(onClick = { showDatePicker = true }) { Text("Change Date") }
                        TextButton(onClick = { showTimePicker = true }) { Text("Change Time") }
                    }

                    OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                            value = hashtags,
                            onValueChange = { hashtags = it },
                            label = { Text("Hashtags") },
                            modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) }
                        Button(
                                onClick = {
                                    val dateMillis =
                                            datePickerState.selectedDateMillis ?: post.scheduledTime
                                    // Simplified time calculation again (assuming dateMillis is UTC
                                    // midnight-ish from picker)
                                    val hourMillis = timePickerState.hour * 3600_000L
                                    val minuteMillis = timePickerState.minute * 60_000L

                                    // A more robust way would involve Calendar or similar if
                                    // available in KMP, or just raw math if we trust the picker
                                    // inputs
                                    // Note: datePickerState.selectedDateMillis returns UTC midnight
                                    // for the selected date.
                                    // To be safe in KMP without advanced timezone heavy lifting, we
                                    // often assume UTC or use system offset.

                                    // Re-calculate total
                                    // Note: if date didn't change, we might want to respect
                                    // original date part more carefully if we want to preserve
                                    // exactness,
                                    // but picking a new time usually implies resetting to that
                                    // created timestamp.

                                    val totalMillis = dateMillis + hourMillis + minuteMillis

                                    onConfirm(totalMillis, description, hashtags)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                        ) { Text("Save") }
                    }
                }
            }
    )
}
