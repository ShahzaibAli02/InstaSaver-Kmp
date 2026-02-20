package com.clipsaver.quickreels.ui.screens.hashtag.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clipsaver.quickreels.ui.theme.PrimaryColor
import com.clipsaver.quickreels.ui.theme.Slate200

@Composable
fun ContentInputSection(text: String, onValueChange: (String) -> Unit) {
    Column {
        Text(
                text = "Describe your content",
                style =
                        TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A) // Slate-900
                        ),
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
        )

        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.dp, Slate200, RoundedCornerShape(16.dp))
        ) {
            BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    textStyle =
                            TextStyle(
                                    fontSize = 16.sp,
                                    color = Color(0xFF0F172A), // Slate-900
                                    lineHeight = 24.sp
                            ),
                    cursorBrush = SolidColor(PrimaryColor),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                    text =
                                            "Type keywords or paste your caption here... e.g., 'Sunset at the beach with friends'",
                                    style =
                                            TextStyle(
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF94A3B8), // Slate-400
                                                    lineHeight = 24.sp
                                            )
                            )
                        }
                        innerTextField()
                    }
            )

            // Character count
            Box(
                    modifier =
                            Modifier.align(Alignment.BottomEnd)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF1F5F9)) // Slate-100
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                        text = "${text.length}/500",
                        style =
                                TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF94A3B8) // Slate-400
                                )
                )
            }
        }
    }
}
