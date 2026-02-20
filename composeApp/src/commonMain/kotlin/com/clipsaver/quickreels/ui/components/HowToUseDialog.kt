package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.theme.DarkTextCharcoal
import com.clipsaver.quickreels.ui.theme.LightTextGray
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.RED
import com.clipsaver.quickreels.ui.theme.SurfaceWhite

@Composable
fun HowToUseDialog(onDismiss: () -> Unit) {
    AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                        text = Strings.HowToUseTitle,
                        style =
                                MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryPurple
                                )
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    InstructionStep(Strings.HowToUseStep1)
                    InstructionStep(Strings.HowToUseStep2)
                    InstructionStep(Strings.HowToUseStep3)
                    InstructionStep(Strings.HowToUseStep4)
                    InstructionStep(Strings.HowToUseStep5)
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                    Text(text = "OK", color = PrimaryPurple, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(16.dp),
            titleContentColor = DarkTextCharcoal,
            textContentColor = LightTextGray
    )
}

@Composable
fun InstructionStep(text: String) {
    Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = DarkTextCharcoal,
            modifier = Modifier.padding(vertical = 4.dp)
    )
}
