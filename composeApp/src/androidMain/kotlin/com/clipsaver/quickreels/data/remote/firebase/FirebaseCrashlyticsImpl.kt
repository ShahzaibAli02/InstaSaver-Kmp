package com.clipsaver.quickreels.data.remote.firebase

import com.clipsaver.quickreels.CrashEvents
import com.google.firebase.crashlytics.FirebaseCrashlytics

class FirebaseCrashlyticsImpl : CrashEvents
{
    override suspend fun logIssue(
        message: String,
        type: CrashEvents.Type,
    )
    {
        FirebaseCrashlytics.getInstance().log(type.name)
        FirebaseCrashlytics.getInstance().recordException(
                Exception(message)
        )
    }
}