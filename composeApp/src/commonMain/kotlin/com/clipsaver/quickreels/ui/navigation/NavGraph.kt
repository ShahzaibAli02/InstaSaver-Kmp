package com.clipsaver.quickreels.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.ads.Ads
import com.clipsaver.quickreels.common.AnalyticsHelper
import com.clipsaver.quickreels.presentation.viewmodels.MainViewModel
import com.clipsaver.quickreels.presentation.viewmodels.SubscriptionViewModel
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.screens.downloads.DownloadsScreen
import com.clipsaver.quickreels.ui.screens.hashtag.HashtagGeneratorScreen
import com.clipsaver.quickreels.ui.screens.home.HomeScreen
import com.clipsaver.quickreels.ui.screens.paywall.PaywallDialogScreen
import com.clipsaver.quickreels.ui.screens.schedule.ScheduleScreen
import com.clipsaver.quickreels.ui.screens.settings.SettingsScreen
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    adsManager: Ads = koinInject(),
    subscriptionViewModel: SubscriptionViewModel = koinViewModel(),
    mainViewModel: MainViewModel = koinViewModel(),
    platform: Platform = koinInject(),
    analyticsHelper: AnalyticsHelper = koinInject(),
)
{


    val isPremium by subscriptionViewModel.isPremium().collectAsState()


    LaunchedEffect(mainViewModel.openCount) {
        if (mainViewModel.shouldShowPayWall(isPremium))
        {
            subscriptionViewModel.showPayWall()
        }
    }


    /**
    ============================== PaywallDialogScreen() ==============================
     **/
    if (subscriptionViewModel.showPayWall)
    {
        PaywallDialogScreen(
                onClose = {
                    analyticsHelper.logEvent("purchase_close")
                    subscriptionViewModel.hidePayWall()
                },
                onAppear = {
                    analyticsHelper.logScreenView(
                            "purchase_view",
                            "PaywallDialogScreen"
                    )
                },
                onPurchaseSuccess = {
                    subscriptionViewModel.hidePayWall()
                    analyticsHelper.logEvent("purchase_successful")
                    platform.showToast("Purchase Successful")
                    subscriptionViewModel.setPremiumStatus(true)
                },
                onPurchaseFailed = {
                    analyticsHelper.logEvent(
                            "purchase_failed",
                            mapOf("error" to it)
                    )
                    platform.showToast(it)
                })

    }


    fun loadAd(adType: Ads.TYPE, onDone: (Boolean) -> Unit)
    {
        adsManager.loadAd(
                adType,
                { success ->

                    onDone(success)
                    if (success)
                    {
                        analyticsHelper.logEvent(
                                Strings.Analytics.Events.AD_LOADED,
                                mapOf(Strings.Analytics.Params.Ad to adType.name)
                        )
                    }

                })
    }

    fun isAdRead(adType: Ads.TYPE) = adsManager.isAdReady(adType)

    fun showAd(adType: Ads.TYPE, onDone: () -> Unit)
    {
        if (adsManager.isAdReady(adType))
        {
            adsManager.showAd(
                    adType,
                    onDone
            )
            analyticsHelper.logEvent(
                    Strings.Analytics.Events.AD_SHOWN,
                    mapOf(Strings.Analytics.Params.Ad to adType.name)
            )
            return
        }
        onDone()
    }
    NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = modifier.padding(horizontal = 16.dp)
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                    onNavigateDownloads = {
                        navController.navigate(BottomNavItem.Downloads.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onShowAd = ::showAd,
                    isAdReady = ::isAdRead,
                    onLoadAd = ::loadAd,
                    isPremium = isPremium,
                    onGoPremium = {
                        subscriptionViewModel.showPayWall()
                    }
            )
        }

        composable(BottomNavItem.Downloads.route) {
            DownloadsScreen(
                    isPremium = isPremium,
                    onShowAd = ::showAd,
                    onLoadAd = ::loadAd
            )
        }

        composable(BottomNavItem.HashTags.route) { HashtagGeneratorScreen() }
        composable(BottomNavItem.Settings.route) {
            SettingsScreen(
                    onGoPremium = {
                        subscriptionViewModel.showPayWall()
                    },
                    isPremium = isPremium,
            )
        }
    }
}
