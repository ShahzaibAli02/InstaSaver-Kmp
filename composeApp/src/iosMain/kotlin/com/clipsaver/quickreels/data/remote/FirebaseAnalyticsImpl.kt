package com.clipsaver.quickreels.data.remote

import com.clipsaver.quickreels.common.AnalyticsHelper

class FirebaseAnalyticsImpl : AnalyticsHelper
{
    override fun logScreenView(screenName: String, screenClass: String)
    {
        TODO("Not yet implemented FirbaseAnalyticsImpl on IOS")
    }

    override fun logEvent(
        eventName: String,
        params: Map<String, Any>?,
    )
    {
        TODO("Not yet implemented FirbaseAnalyticsImpl on IOS")
    }
}