package com.clipsaver.quickreels.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.clipsaver.quickreels.AppConfig
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.presentation.viewmodels.MainViewModel
import com.clipsaver.quickreels.ui.components.RatingDialog
import com.clipsaver.quickreels.ui.navigation.BottomNavItem
import com.clipsaver.quickreels.ui.navigation.NavGraph
import com.clipsaver.quickreels.ui.theme.LightTextGray
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite
import com.clipsaver.quickreels.common.AnalyticsHelper
import com.clipsaver.quickreels.ui.Strings
import androidx.compose.runtime.LaunchedEffect
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

@Composable fun MainScreen(viewModel: MainViewModel = koinViewModel())
{
    val showRatingDialog by viewModel.showRatingDialog.collectAsState()
    val platform: Platform = getKoin().get()
    val appConfig: AppConfig = getKoin().get()
    val analyticsHelper: AnalyticsHelper = getKoin().get()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        analyticsHelper.logScreenView(
                Strings.Analytics.Screens.Main,
                Strings.Analytics.Screens.Main
        )
    }
    LaunchedEffect(showRatingDialog){
        if(showRatingDialog){
            platform.requestReview(onDone = {
                viewModel.onRateClicked()
            })
        }
    }



    val navController = rememberNavController()
    val items = listOf(
            BottomNavItem.Home, //                    BottomNavItem.Schedule,
            BottomNavItem.Downloads,
            BottomNavItem.HashTags,
            BottomNavItem.Settings
    )

    Scaffold(
            bottomBar = {
                NavigationBar(
                        containerColor = SurfaceWhite,
                        contentColor = PrimaryPurple
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                                icon = {
                                    Icon(
                                            modifier = Modifier.size(23.dp),
                                            painter = painterResource(item.icon),
                                            contentDescription = item.title,
                                            tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.8f)
                                    )
                                },
                                label = {

                                    Text(
                                            item.title,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.labelSmall,
                                            overflow = TextOverflow.Ellipsis
                                    )
                                },
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = PrimaryPurple,
                                        selectedTextColor = PrimaryPurple,
                                        unselectedIconColor = LightTextGray,
                                        unselectedTextColor = LightTextGray,
                                        indicatorColor = Color.Transparent
                                )
                        )
                    }
                }
            }) { innerPadding -> //        Text("HEY",
        //                modifier = Modifier.padding(innerPadding))

        NavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                mainViewModel = viewModel
        )
    }
}
