package com.clipsaver.quickreels.data.shared

import com.clipsaver.quickreels.domain.models.Video
import kotlinx.coroutines.flow.MutableSharedFlow

interface Events {
    val events: MutableSharedFlow<EventType>

    suspend fun sendEvent(event: EventType)
}

sealed class EventType {

    data class CancelDownload(val video: Video) : EventType()
    data class RetryDownload(val video: Video) : EventType()
    data class DeleteVideo(val video: Video) : EventType()
    data class ShareVideo(val videoPath: String) : EventType()
    object None : EventType()
}
