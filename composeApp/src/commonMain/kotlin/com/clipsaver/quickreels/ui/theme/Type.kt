package com.clipsaver.quickreels.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import reelsaverpro.composeapp.generated.resources.Epilogue_Black
import reelsaverpro.composeapp.generated.resources.Epilogue_Bold
import reelsaverpro.composeapp.generated.resources.Epilogue_ExtraBold
import reelsaverpro.composeapp.generated.resources.Epilogue_Medium
import reelsaverpro.composeapp.generated.resources.Epilogue_Regular
import reelsaverpro.composeapp.generated.resources.Epilogue_SemiBold
import reelsaverpro.composeapp.generated.resources.Res

@Composable
fun EpilogueFontFamily() =
        FontFamily(
                Font(Res.font.Epilogue_Regular, FontWeight.Normal),
                Font(Res.font.Epilogue_Medium, FontWeight.Medium),
                Font(Res.font.Epilogue_ExtraBold, FontWeight.ExtraBold),
                Font(Res.font.Epilogue_Black, FontWeight.Black),
                Font(Res.font.Epilogue_SemiBold, FontWeight.SemiBold),
        )

@Composable
fun AppTypography() =
        Typography(
                displayLarge =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 57.sp,
                                lineHeight = 64.sp,
                                letterSpacing = (-0.25).sp
                        ),
                displayMedium =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 45.sp,
                                lineHeight = 52.sp,
                                letterSpacing = 0.sp
                        ),
                displaySmall =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp,
                                lineHeight = 44.sp,
                                letterSpacing = 0.sp
                        ),
                headlineLarge =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                lineHeight = 40.sp,
                                letterSpacing = 0.sp
                        ),
                headlineMedium =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                lineHeight = 36.sp,
                                letterSpacing = 0.sp
                        ),
                headlineSmall =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                lineHeight = 32.sp,
                                letterSpacing = 0.sp
                        ),
                titleLarge =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                lineHeight = 28.sp,
                                letterSpacing = 0.sp
                        ),
                titleMedium =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.15.sp
                        ),
                titleSmall =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.1.sp
                        ),
                bodyLarge =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp
                        ),
                bodyMedium =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.25.sp
                        ),
                bodySmall =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.4.sp
                        ),
                labelLarge =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.1.sp
                        ),
                labelMedium =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp
                        ),
                labelSmall =
                        TextStyle(
                                fontFamily = EpilogueFontFamily(),
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 0.5.sp
                        )
        )
