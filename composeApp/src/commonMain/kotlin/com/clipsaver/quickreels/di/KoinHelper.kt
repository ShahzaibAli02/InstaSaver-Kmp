package com.clipsaver.quickreels.di

import com.clipsaver.quickreels.AppCheck
import com.clipsaver.quickreels.AppConfig
import com.clipsaver.quickreels.CrashEvents
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind

fun initKoin(appCheck: AppCheck, appConfig: AppConfig, crashEvents: CrashEvents, config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
                appModule.apply {
                    single { appCheck } bind AppCheck::class
                    single { appConfig } bind AppConfig::class
                    single { crashEvents } bind CrashEvents::class
                }
        )
    }
}

