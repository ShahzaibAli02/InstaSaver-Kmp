package com.clipsaver.quickreels

// commonMain
interface AppConfig {
    fun onConfig(onResponse : (Boolean, String) -> Unit)
}
interface AppCheck {
    suspend fun getAppCheckToken() : String?
}
interface CrashEvents {
    suspend fun logIssue(message : String, type : Type = Type.INFO)
    enum class Type {
        ERROR,
        INFO,
        DEBUG,
        WARNING
    }
}

// Shared class that stores the callback
