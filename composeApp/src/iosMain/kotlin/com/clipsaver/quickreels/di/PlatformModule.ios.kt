package com.clipsaver.quickreels.di

import com.clipsaver.quickreels.AppCheck
import com.clipsaver.quickreels.IOSPlatform
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.common.AnalyticsHelper

import com.clipsaver.quickreels.data.local.DatabaseDriverFactory
import com.clipsaver.quickreels.data.local.FileManager
import com.clipsaver.quickreels.data.local.FileManagerImpl
import com.clipsaver.quickreels.data.local.IOSDatabaseDriverFactory
import com.clipsaver.quickreels.data.local.IOSLocalSharedPref
import com.clipsaver.quickreels.data.local.LocalSharedPref
import com.clipsaver.quickreels.data.remote.FirebaseAnalyticsImpl
import com.clipsaver.quickreels.utils.ClipBoardHelper
import com.clipsaver.quickreels.utils.ClipBoardHelperImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::FileManagerImpl).bind<FileManager>()
    single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
    single<ClipBoardHelper> { ClipBoardHelperImpl() }
    single<Platform> { IOSPlatform() }
    single<LocalSharedPref> { IOSLocalSharedPref() }
    single<AnalyticsHelper> { FirebaseAnalyticsImpl() }

//    single<AppCheck>{  object : AppCheck
//        {
//            override suspend fun getAppCheckToken(): String?
//            {
//                return  "EMPTY"
////               return SharedController.appCheck?.getAppCheckToken()
//            }
//        }
//    }
}
