package com.clipsaver.quickreels.ui.screens.hashtag

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clipsaver.quickreels.data.remote.models.Tag
import com.clipsaver.quickreels.presentation.viewmodels.HashtagGeneratorViewModel
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.components.HeaderTitle
import com.clipsaver.quickreels.ui.screens.hashtag.components.GenerateButton
import com.clipsaver.quickreels.ui.screens.hashtag.components.TrendingTopicChip
import com.clipsaver.quickreels.ui.theme.*
import com.clipsaver.quickreels.utils.Util
import org.koin.compose.viewmodel.koinViewModel
import com.clipsaver.quickreels.common.AnalyticsHelper
import org.koin.compose.getKoin


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HashtagGeneratorScreen(
        viewModel: HashtagGeneratorViewModel = koinViewModel(),
) {
    val inputText by viewModel.inputText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val generatedHashtags by viewModel.generatedHashtags.collectAsState()
    val focusManager = LocalFocusManager.current
    val analyticsHelper: AnalyticsHelper = getKoin().get()

    LaunchedEffect(Unit) {
        analyticsHelper.logScreenView(Strings.Analytics.Screens.HashtagGenerator, Strings.Analytics.Screens.HashtagGenerator)
    }

    val colorScheme = MaterialTheme.colorScheme
    val borderColor = colorScheme.onSurfaceVariant
    val primaryColor = colorScheme.primary
    val textColor = colorScheme.onSurface

    Scaffold(
            containerColor = colorScheme.background,
            topBar = {
                HeaderTitle(modifier = Modifier.fillMaxWidth()
                    .padding(top = 30.dp)
                    .padding(bottom = 24.dp)

                        ,title = Strings.HashTagGenertor)
            }
    ) { paddingValues ->
        LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(bottom = 24.dp)
        ) { // Input Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                            text = "Describe your content",
                            style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor
                            ),
                            modifier = Modifier.padding(
                                    bottom = 12.dp,
                                    start = 4.dp
                            )
                    )

                    Box(
                            modifier = Modifier.fillMaxWidth().height(160.dp)
                                .clip(RoundedCornerShape(16.dp)).background(colorScheme.surface).border(
                                        1.dp,
                                        borderColor,
                                        RoundedCornerShape(16.dp)
                                )
                    ) {
                        BasicTextField(
                                value = inputText,
                                onValueChange = {
                                    if (it.length <= 500) viewModel.onInputTextChanged(it)
                                },
                                modifier = Modifier.fillMaxSize().padding(20.dp),
                                textStyle = TextStyle(
                                        fontSize = 16.sp,
                                        color = textColor,
                                        lineHeight = 24.sp
                                ),
                                cursorBrush = SolidColor(primaryColor),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                decorationBox = { innerTextField ->
                                    if (inputText.isEmpty())
                                    {
                                        Text(
                                                text = "Type keywords or paste your caption here... e.g., 'Sunset at the beach with friends'",
                                                style = TextStyle(
                                                        fontSize = 16.sp,
                                                        color = Color(0xFF94A3B8),
                                                        lineHeight = 24.sp
                                                )
                                        )
                                    }
                                    innerTextField()
                                })

                        // Char count
                        Box(
                                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                                    .clip(RoundedCornerShape(8.dp)).background(Color(0xFFF1F5F9))
                                    .padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                    )
                        ) {
                            Text(
                                    text = "${inputText.length}/500",
                                    style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF94A3B8)
                                    )
                            )
                        }
                    }
                }
            }

            // Trending Topics
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column {
                    Row(
                            modifier = Modifier.fillMaxWidth().padding(
                                        bottom = 12.dp,
                                        start = 4.dp,
                                        end = 4.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = "Trending Topics",
                                style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                )
                        )
                        Text(
                                text = "View All",
                                style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryColor
                                ),
                                modifier = Modifier.clickable { /* Handle View All */ })
                    }

                    LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(viewModel.trendingCategories) { category ->
                            TrendingTopicChip(
                                    category = category,
                                    onClick = {
                                        viewModel.onInputTextChanged(
                                                "Create tags on this category ${category.name}"
                                        )
                                    })
                        }
                    }
                }
            }

            // Generate Button
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Shimmer Effect
                val transition = rememberInfiniteTransition()
                val translateAnim by transition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1000f,
                        animationSpec = infiniteRepeatable(
                                animation = tween(
                                        durationMillis = 2000,
                                        easing = LinearEasing
                                ),
                                repeatMode = RepeatMode.Restart
                        )
                )


                GenerateButton(
                        text =  "Generate Hashtags",
                        isLoading = isLoading,
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.generateHashtags(inputText)
                        }
                )
//                Button(
//                        onClick = {
//                            focusManager.clearFocus()
//                            viewModel.generateHashtags(inputText)
//                        },
//                        modifier = Modifier.fillMaxWidth().height(56.dp).shadow(
//                                    elevation = 10.dp,
//                                    shape = RoundedCornerShape(28.dp),
//                                    spotColor = primaryColor.copy(alpha = 0.35f)
//                            ),
//                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
//                        shape = RoundedCornerShape(28.dp),
//                        enabled = !isLoading
//                ) {
//                    Box(contentAlignment = Alignment.Center) { // Applying shimmer overlay
//                        Box(modifier = Modifier.matchParentSize().background(shimmerBrush))
//
//                        Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.Center
//                        ) {
//                            if (isLoading)
//                            {
//                                CircularProgressIndicator(
//                                        modifier = Modifier.size(24.dp),
//                                        color = Color.White,
//                                        strokeWidth = 2.dp
//                                )
//                            } else
//                            {
//                                Icon(
//                                        imageVector = Icons.Filled.AutoAwesome,
//                                        contentDescription = null,
//                                        tint = Color.White,
//                                        modifier = Modifier.size(22.dp)
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                        text = "Generate Hashtags",
//                                        style = TextStyle(
//                                                fontSize = 16.sp,
//                                                fontWeight = FontWeight.Bold,
//                                                letterSpacing = 0.5.sp,
//                                                color = Color.White
//                                        )
//                                )
//                            }
//                        }
//                    }
//                }
            }

            // Generated Results
            if (generatedHashtags.isNotEmpty())
            {
                val allSelected = generatedHashtags.all { it.isSelected }
                val selectedCount = generatedHashtags.count { it.isSelected }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column { // Results Header
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                        text = "Generated Results",
                                        style = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                        modifier = Modifier.background(
                                                    primaryColor.copy(alpha = 0.2f),
                                                    RoundedCornerShape(50)
                                            ).padding(
                                                    horizontal = 8.dp,
                                                    vertical = 2.dp
                                            )
                                ) {
                                    Text(
                                            text = "$selectedCount/${generatedHashtags.size}",
                                            style = TextStyle(
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = primaryColor
                                            )
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { // Removed Sort Icon as requested
                                IconButton(
                                        onClick = { viewModel.regenrateTags() },
                                        modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                            imageVector = Icons.Default.Shuffle,
                                            contentDescription = "Shuffle",
                                            tint = Color(0xFF64748B)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Results Box
                        Box(
                                modifier = Modifier.fillMaxWidth().background(
                                            colorScheme.surface,
                                            RoundedCornerShape(16.dp)
                                    ).border(
                                            1.dp,
                                            borderColor,
                                            RoundedCornerShape(16.dp)
                                    ).padding(16.dp)
                        ) {
                            Column {
                                FlowRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    generatedHashtags.forEachIndexed { index, tagUiState ->
                                        TagChip(
                                                tagItem = tagUiState.tag,
                                                isSelected = tagUiState.isSelected,
                                                onClick = { viewModel.toggleSelection(index) })
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = borderColor.copy(alpha = 0.5f))
                                Spacer(modifier = Modifier.height(16.dp))

                                // Action Buttons
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                            onClick = { viewModel.toggleSelectAll() },
                                            colors = ButtonDefaults.textButtonColors(
                                                    contentColor = Color(0xFF475569)
                                            ),
                                            modifier = Modifier.background(
                                                        Color(0xFFF1F5F9),
                                                        RoundedCornerShape(8.dp)
                                                ).height(32.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp)
                                    ) {
                                        Icon(
                                                imageVector = if (allSelected) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                                text = if (allSelected) "Deselect All" else "Select All",
                                                style = TextStyle(
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                )
                                        )
                                    }

                                    Button(
                                            onClick = { viewModel.copyHashtags() },
                                            colors = ButtonDefaults.buttonColors(
                                                    containerColor = primaryColor.copy(alpha = 0.1f),
                                                    contentColor = primaryColor
                                            ),
                                            shape = RoundedCornerShape(50),
                                            contentPadding = PaddingValues(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                            ),
                                            elevation = null
                                    ) {
                                        Icon(
                                                imageVector = Icons.Outlined.ContentCopy,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                                text = if (selectedCount > 0) "Copy ($selectedCount)" else "Copy",
                                                style = TextStyle(
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }}}
// ... (Pro Tip section remians same) ...
// ... (TrendingTopicChip remains same) ...

@Composable
fun TagChip(
    tagItem: Tag, 
    isSelected: Boolean, 
    onClick: () -> Unit
) {
    val popularity = tagItem.popularity ?: 0L

    // Determine colors based on popularity
    val (baseLabelColor, baseChipBg, baseChipBorder, baseCountColor, baseCountBg) =
        when {
            popularity >= 100_000_000 -> { // Viral (Green)
                listOf(
                    Color(0xFF1E293B),
                    Color(0xFFF8FAFC),
                    Color(0xFFF1F5F9),
                    Color(0xFF16A34A),
                    Color(0xFFDCFCE7)
                )
            }
            popularity >= 10_000_000 -> { // Popular (Blue)
                listOf(
                    Color(0xFF1E293B),
                    Color(0xFFF8FAFC),
                    Color(0xFFF1F5F9),
                    Color(0xFF2563EB),
                    Color(0xFFDBEAFE)
                )
            }
            else -> { // Niche (Gray)
                listOf(
                    Color(0xFF334155),
                    Color(0xFFF8FAFC),
                    Color(0xFFF1F5F9),
                    Color(0xFF64748B),
                    Color(0xFFE2E8F0)
                )
            }
        }

    // Override colors if selected
    val finalChipBg = if (isSelected) PrimaryColor.copy(alpha = 0.1f) else baseChipBg
    val finalChipBorder = if (isSelected) PrimaryColor else baseChipBorder
    val finalLabelColor = if (isSelected) PrimaryColor else baseLabelColor

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(finalChipBg)
            .border(if (isSelected) 1.5.dp else 1.dp, finalChipBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tagItem.tag.orEmpty(),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = if (popularity >= 10_000_000 || isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = finalLabelColor
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .background(baseCountBg, RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = Util.formatCount(popularity),
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = baseCountColor
                )
            )
        }
    }

    }
