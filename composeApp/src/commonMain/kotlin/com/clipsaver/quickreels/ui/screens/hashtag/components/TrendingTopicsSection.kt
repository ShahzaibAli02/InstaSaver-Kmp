package com.clipsaver.quickreels.ui.screens.hashtag.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.clipsaver.quickreels.models.TrendingCategory
import com.clipsaver.quickreels.ui.theme.Slate200

@Composable
fun TrendingTopicsSection(
        categories: List<TrendingCategory>,
        onCategoryClick: (TrendingCategory) -> Unit,
        onViewAllClick: () -> Unit
) {
    Column {
        Row(
                modifier =
                        Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                    text = "Trending Topics",
                    style =
                            TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A) // Slate-900
                            )
            )
            // Text(
            //     text = "View All",
            //     style = TextStyle(
            //         fontSize = 12.sp,
            //         fontWeight = FontWeight.Bold,
            //         color = PrimaryColor
            //     ),
            //     modifier = Modifier.clickable { onViewAllClick() }
            // )
        }

        LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(categories) { category ->
                TrendingTopicChip(category, onClick = { onCategoryClick(category) })
            }
        }
    }
}

@Composable
fun TrendingTopicChip(category: TrendingCategory, onClick: () -> Unit) {
    Row(
            modifier =
                    Modifier.height(40.dp)
                            .clip(RoundedCornerShape(50)) // Full pill shape
                            .background(Color.White)
                            .border(1.dp, Slate200, RoundedCornerShape(50))
                            .clickable { onClick() }
                            .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
    ) {
        Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = category.color,
                modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
                text = category.name,
                style =
                        TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF334155) // Slate-700
                        )
        )
    }
}
