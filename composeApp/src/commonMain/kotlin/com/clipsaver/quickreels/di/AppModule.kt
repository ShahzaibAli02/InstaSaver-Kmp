package com.clipsaver.quickreels.di

import com.clipsaver.quickreels.data.remote.FakeNetworkHelperImpl
import com.clipsaver.quickreels.data.remote.NetworkHelper
import com.clipsaver.quickreels.data.remote.NetworkHelperImpl
import com.clipsaver.quickreels.data.repositories.DownloadsRepositoryImpl
import com.clipsaver.quickreels.data.repositories.HashtagRepoImpl
import com.clipsaver.quickreels.data.repositories.HomeRepositoryImpl
import com.clipsaver.quickreels.data.repositories.LocalRepositoryImpl
import com.clipsaver.quickreels.data.repositories.ScheduleRepositoryImpl
import com.clipsaver.quickreels.data.repositories.SettingsRepositoryImpl
import com.clipsaver.quickreels.data.repositories.SubscriptionRepositoryImpl
import com.clipsaver.quickreels.data.shared.Events
import com.clipsaver.quickreels.data.shared.EventsImpl
import com.clipsaver.quickreels.db.AppDatabase
import com.clipsaver.quickreels.domain.repositories.DownloadsRepository
import com.clipsaver.quickreels.domain.repositories.HashtagRepo
import com.clipsaver.quickreels.domain.repositories.HomeRepository
import com.clipsaver.quickreels.domain.repositories.LocalRepository
import com.clipsaver.quickreels.domain.repositories.ScheduleRepository
import com.clipsaver.quickreels.domain.repositories.SettingsRepository
import com.clipsaver.quickreels.domain.repositories.SubscriptionRepository
import com.clipsaver.quickreels.presentation.viewmodels.DownloadsViewModel
import com.clipsaver.quickreels.presentation.viewmodels.HashtagGeneratorViewModel
import com.clipsaver.quickreels.presentation.viewmodels.HomeViewModel
import com.clipsaver.quickreels.presentation.viewmodels.MainViewModel
import com.clipsaver.quickreels.presentation.viewmodels.ScheduleViewModel
import com.clipsaver.quickreels.presentation.viewmodels.SettingsViewModel
import com.clipsaver.quickreels.presentation.viewmodels.SubscriptionViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule =
        module {
            // Define domain layer dependencies (UseCases)
        }

val dataModule = module {
    single {
        val driver = get<com.clipsaver.quickreels.data.local.DatabaseDriverFactory>().createDriver()
        AppDatabase(driver)
    }
    singleOf(::LocalRepositoryImpl).bind<LocalRepository>()
    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    singleOf(::SettingsRepositoryImpl).bind<SettingsRepository>()
    singleOf(::DownloadsRepositoryImpl).bind<DownloadsRepository>()
    singleOf(::HashtagRepoImpl).bind<HashtagRepo>()
    singleOf(::NetworkHelperImpl).bind<NetworkHelper>()

//    singleOf(::FakeNetworkHelperImpl).bind<NetworkHelper>()
    singleOf(::EventsImpl).bind<Events>()
    singleOf(::ScheduleRepositoryImpl).bind<ScheduleRepository>()
    singleOf(::SubscriptionRepositoryImpl).bind<SubscriptionRepository>()
}

val presentationModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::DownloadsViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::HashtagGeneratorViewModel)
    viewModelOf(::SubscriptionViewModel)
    viewModelOf(::ScheduleViewModel)
}

val appModule = module { includes(domainModule, dataModule, presentationModule, platformModule) }
