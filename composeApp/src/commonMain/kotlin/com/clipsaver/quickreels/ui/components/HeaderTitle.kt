package com.clipsaver.quickreels.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.theme.PrimaryPurple

@Composable
fun HeaderTitle(modifier: Modifier = Modifier, title: String) {


    Box(
            modifier = modifier
            ,
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = title,
                style =
                    MaterialTheme.typography.headlineLarge.copy( // Custom style with shadow
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            shadow =
                                Shadow(
                                        color = Color.Black.copy(alpha = 0.25f),
                                        offset =
                                            androidx.compose.ui.geometry.Offset(2f, 4f),
                                        blurRadius = 4f
                                )
                    )
        )
    }
}
