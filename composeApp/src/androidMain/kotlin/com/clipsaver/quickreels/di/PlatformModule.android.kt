package com.clipsaver.quickreels.di

import com.clipsaver.ads.AdmobAdsImpl
import com.clipsaver.quickreels.AndroidPlatform
import com.clipsaver.quickreels.utils.ClipBoardHelper
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.ads.Ads
import com.clipsaver.quickreels.common.AnalyticsHelper
import com.clipsaver.quickreels.data.local.AndroidDatabaseDriverFactory
import com.clipsaver.quickreels.data.local.AndroidLocalSharedPref
import com.clipsaver.quickreels.data.local.DatabaseDriverFactory
import com.clipsaver.quickreels.data.local.FileManager
import com.clipsaver.quickreels.data.local.FileManagerImpl
import com.clipsaver.quickreels.data.local.LocalSharedPref
import com.clipsaver.quickreels.data.remote.AnalyticsHelperImpl
import com.clipsaver.quickreels.utils.ClipBoardHelperImpl
import com.clipsaver.quickreels.utils.DeepLinkHelper
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::FileManagerImpl).bind<FileManager>()
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
    single<ClipBoardHelper> { ClipBoardHelperImpl(androidContext()) }
    single<Platform> { AndroidPlatform(androidContext()) }
    single<AnalyticsHelper> { AnalyticsHelperImpl(FirebaseAnalytics.getInstance(androidContext())) }

    single<LocalSharedPref> { AndroidLocalSharedPref(androidContext()) }
    single<Ads> { AdmobAdsImpl(androidContext()) }
    single { DeepLinkHelper() }
}
