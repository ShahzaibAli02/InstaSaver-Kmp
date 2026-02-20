package com.clipsaver.quickreels.domain.repositories

import com.clipsaver.quickreels.data.shared.Events
import com.clipsaver.quickreels.domain.models.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface DownloadsRepository   : Events , LocalRepository


