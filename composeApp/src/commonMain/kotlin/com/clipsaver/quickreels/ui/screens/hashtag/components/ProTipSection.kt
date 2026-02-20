package com.clipsaver.quickreels.ui.screens.hashtag.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProTipSection() {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F5F9)) // Slate-100
                            .padding(12.dp),
            verticalAlignment = Alignment.Top
    ) {
        Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = Color(0xFFEAB308), // Yellow-500
                modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
                text =
                        buildAnnotatedString {
                            withStyle(
                                    style =
                                            SpanStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF334155) // Slate-700
                                            )
                            ) { append("Pro Tip: ") }
                            append(
                                    "Mix popular hashtags (1M+ posts) with niche ones (under 50k) to maximize your reach."
                            )
                        },
                style =
                        TextStyle(
                                fontSize = 12.sp,
                                color = Color(0xFF64748B), // Slate-500
                                lineHeight = 18.sp
                        )
        )
    }
}
