package com.clipsaver.quickreels.data.repositories

import com.clipsaver.quickreels.data.shared.Events
import com.clipsaver.quickreels.data.shared.EventsImpl
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.domain.repositories.DownloadsRepository
import com.clipsaver.quickreels.domain.repositories.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DownloadsRepositoryImpl(
    private val localRepositoryImpl: LocalRepository,
        private val eventsImpl: Events) :
    DownloadsRepository,

    LocalRepository by localRepositoryImpl,
    Events by eventsImpl
{


}
