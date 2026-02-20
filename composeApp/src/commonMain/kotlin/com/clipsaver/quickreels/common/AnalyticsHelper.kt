package com.clipsaver.quickreels.common

interface AnalyticsHelper {
    fun logScreenView(screenName: String, screenClass: String)
    fun logEvent(eventName: String, params: Map<String, Any>? = null)
}
