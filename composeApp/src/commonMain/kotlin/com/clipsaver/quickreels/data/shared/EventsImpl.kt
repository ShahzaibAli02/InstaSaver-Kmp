package com.clipsaver.quickreels.data.shared

import kotlinx.coroutines.flow.MutableSharedFlow

class EventsImpl : Events {
    override val events = MutableSharedFlow<EventType>()

    override suspend fun sendEvent(event: EventType) {
        events.emit(event)
    }
}