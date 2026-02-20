package com.clipsaver.quickreels.data.repositories

import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.data.local.FileManager
import com.clipsaver.quickreels.data.remote.FakeNetworkHelperImpl
import com.clipsaver.quickreels.data.remote.NetworkHelper
import com.clipsaver.quickreels.data.shared.EventType
import com.clipsaver.quickreels.data.shared.Events
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.domain.repositories.HomeRepository
import com.clipsaver.quickreels.domain.repositories.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

class HomeRepositoryImpl(
        private val localRepository: LocalRepository,
        private val networkHelperImpl: NetworkHelper,
        private val fileManager: FileManager,
        private val eventsImpl: Events,
        private val platformImpl: Platform,
) :
        HomeRepository,
        LocalRepository by localRepository,
        FileManager by fileManager,
        NetworkHelper by networkHelperImpl,
        Events by eventsImpl,
        Platform by platformImpl

