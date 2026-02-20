package com.clipsaver.quickreels.ui.navigation

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import com.clipsaver.quickreels.ui.Strings
import org.jetbrains.compose.resources.DrawableResource
import reelsaverpro.composeapp.generated.resources.Res
import reelsaverpro.composeapp.generated.resources.ic_collection
import reelsaverpro.composeapp.generated.resources.ic_home
import reelsaverpro.composeapp.generated.resources.ic_schedule
import reelsaverpro.composeapp.generated.resources.ic_settings
import reelsaverpro.composeapp.generated.resources.ic_tag

sealed class BottomNavItem(val route: String, val title: String, val icon: DrawableResource) {
        object Home : BottomNavItem("home", Strings.Tabs.Home, Res.drawable.ic_home)
        object Downloads :
                BottomNavItem(
                        "downloads",
                        Strings.Tabs.Downloads,
                        Res.drawable.ic_collection
                ) // Placeholder icon
        object Settings :
                BottomNavItem("settings", Strings.Tabs.Settings, Res.drawable.ic_settings)

        object Schedule : BottomNavItem("schedule", "Schedule", Res.drawable.ic_schedule)

        object HashTags : BottomNavItem("#Tags", Strings.Tabs.HashTags, Res.drawable.ic_tag)
}
