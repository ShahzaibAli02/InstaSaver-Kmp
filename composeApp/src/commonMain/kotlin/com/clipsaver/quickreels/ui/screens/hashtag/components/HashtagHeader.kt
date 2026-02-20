package com.clipsaver.quickreels.ui.screens.hashtag.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
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

@Composable
fun HashtagHeader(onBackClick: () -> Unit, onHistoryClick: () -> Unit = {}) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Transparent)
        ) {
            Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF0F172A) // Slate-900
            )
        }

        Text(
                text = "Hashtag Generator",
                style =
                        TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A) // Slate-900
                        )
        )

        IconButton(
                onClick = onHistoryClick,
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Transparent)
        ) {
            Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    tint = Color(0xFF64748B) // Slate-500
            )
        }
    }
}
