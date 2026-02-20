package com.clipsaver.quickreels.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.presentation.viewmodels.SettingsViewModel
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.components.HeaderTitle
import com.clipsaver.quickreels.ui.theme.BackgroundOffWhite
import com.clipsaver.quickreels.ui.theme.DarkTextCharcoal
import com.clipsaver.quickreels.ui.theme.LightTextGray
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite
import com.clipsaver.quickreels.utils.Constants
import org.koin.compose.getKoin
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.clipsaver.quickreels.common.AnalyticsHelper
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import reelsaverpro.composeapp.generated.resources.Res
import reelsaverpro.composeapp.generated.resources.ic_crown

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onGoPremium: () -> Unit,
    isPremium: Boolean,
) {

        val platform : Platform = getKoin().get()
        val uriHandler = LocalUriHandler.current
        val analyticsHelper: AnalyticsHelper = getKoin().get()

        LaunchedEffect(Unit) {
            analyticsHelper.logScreenView(Strings.Analytics.Screens.Settings, Strings.Analytics.Screens.Settings)
        }


    Scaffold(
            topBar = { HeaderTitle(modifier = Modifier.fillMaxWidth()
                .padding(top = 30.dp)
                .padding(bottom = 24.dp)

                    ,title = Strings.Settings) },
            content = { paddingValues ->
                Column(modifier =
                    Modifier.fillMaxSize()
                        .background(BackgroundOffWhite)
                        .padding(paddingValues)
                        .padding(2.dp)
                        .verticalScroll(rememberScrollState())
                ) {



                    // Go Premium Button
                    if (!isPremium)
                    {
                        SettingsButton(
                                title = "Go Premium",
                                subtitle = "Unlock all features and remove limits",
                                icon = Icons.Default.WorkspacePremium,
                                iconBackgroundColor = Color(0xFFFEF3C7), // amber-100
                                iconTintColor = Color(0xFFD97706), // amber-600
                                onClick = onGoPremium,
                                showSubtitle = true
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }


                    // Toggles Section
                    //                SettingsGroup {
                    ////                        SettingsToggleRow(
                    ////                                title = "Auto Paste Link",
                    ////                                checked = viewModel.isAutoPasteEnabled,
                    ////                                onCheckedChange = { viewModel.toggleAutoPaste(it) },
                    ////                                showDivider = true
                    ////                        )
                    //                        SettingsToggleRow(
                    //                                title = "Save to Photos Automatically",
                    //                                checked = viewModel.isSaveToPhotosEnabled,
                    //                                onCheckedChange = {
                    //
                    //                                    if(it){
                    //                                        platform.requestAuthorizationToSaveFiles()
                    //                                    }
                    //                                    viewModel.toggleSaveToPhotos(it) },
                    //                                showDivider = false
                    //                        )
                    //                }

//                    Spacer(modifier = Modifier.height(24.dp))

                    // Actions Section
                    SettingsGroup {
                        //                        SettingsActionRow(
                        //                                title = "Clear Cache",
                        //                                icon = Icons.Default.Delete,
                        //                                onClick = { /* TODO: Clear Cache */},
                        //                                showDivider = true
                        //                        )
                        SettingsActionRow(
                                title = "Rate the App",
                                icon = Icons.Default.Star,
                                onClick = { uriHandler.openUri(platform.storeLink)},
                                showDivider = false
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Legal & Contact Section
                    SettingsGroup {
                        SettingsActionRow(
                                title = "Privacy Policy",
                                icon = Icons.Default.Policy,
                                onClick = { uriHandler.openUri(Constants.PRIVACY_POLICY_URL) },
                                showDivider = true
                        )
                        SettingsActionRow(
                                title = "Terms Of Use",
                                icon = Icons.Default.Description,
                                onClick = { uriHandler.openUri(Constants.TERMS_OF_USE_URL) },
                                showDivider = true
                        )
                        SettingsActionRow(
                                title = "Contact Us",
                                icon = Icons.Default.Email,
                                onClick = {
                                    uriHandler.openUri("mailto:${Constants.CONTACT_EMAIL}")
                                },
                                showDivider = false
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // App Version
                    Text(
                            text = "App Version ${platform.version}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightTextGray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
    )

}

@Composable
fun SettingsGroup(content: @Composable () -> Unit) {
        Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) { Column { content() } }
}

@Composable
fun SettingsToggleRow(
        title: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        showDivider: Boolean
) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Text(
                        text = title,
                        style =
                                MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = DarkTextCharcoal
                                )
                )
                Switch(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        colors =
                                SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = PrimaryPurple,
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = LightTextGray
                                )
                )
        }
        if (showDivider) {
                HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = LightTextGray.copy(alpha = 0.2f)
                )
        }
}

@Composable
fun SettingsActionRow(title: String, icon: ImageVector, onClick: () -> Unit, showDivider: Boolean) {
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .clickable { onClick() }
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                        text = title,
                        style =
                                MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = DarkTextCharcoal
                                ),
                        modifier = Modifier.weight(1f)
                )
                Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = LightTextGray,
                        modifier = Modifier.size(24.dp)
                )
        }
        if (showDivider) {
                HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = LightTextGray.copy(alpha = 0.2f)
                )
        }
}

@Composable fun SettingsButton(
    title: String,
    subtitle: String = "",
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTintColor: Color,
    onClick: () -> Unit,
    showSubtitle: Boolean = false,
    trailingIcon: ImageVector = Icons.Default.ChevronRight,
    isMonospaceSubtitle: Boolean = false,
)
{
    Row(
            modifier = Modifier.fillMaxWidth().shadow(
                    elevation = 1.5.dp, // shadow-sm
                    shape = RoundedCornerShape(
                            20.dp
                    ), // rounded-lg (~0.5rem * 2 or just 16dp)
                    spotColor = Color.Black.copy(alpha = 0.05f)
            ).clip(RoundedCornerShape(20.dp)).background(
                    MaterialTheme.colorScheme.surface
            ) // bg-white / dark:bg-[#1C232E]
                .clickable(onClick = onClick).padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                ).height(
                        if (showSubtitle) 60.dp else 50.dp
                ) // min-h-[5rem] approx 80dp or smaller for mobile?
                // Re-adjusting height logic to be flexible or min height
                .then(
                        Modifier.height(if (showSubtitle) 70.dp else 56.dp)
                ), // Adjusting for ease
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
        ) {
            Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                        .background(iconBackgroundColor),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        painter = painterResource(Res.drawable.ic_crown),
                        contentDescription = null,
                        tint = iconTintColor,
                        modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(verticalArrangement = Arrangement.Center) {
                Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp,
                                lineHeight = 16.sp // leading-none
                        )
                )
                if (showSubtitle)
                {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                            text = subtitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(
                                            0xFF9CA3AF
                                    ), // text-gray-500
                                    fontSize = 12.sp,
                                    fontFamily = if (isMonospaceSubtitle) androidx.compose.ui.text.font.FontFamily.Monospace
                                    else null
                            )
                    )
                }
            }
        }

        Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = Color(0xFF9CA3AF), // text-gray-400
                modifier = Modifier.size(24.dp)
        )
    }
}