package com.clipsaver.quickreels

interface Platform
{
    val name: String
    val version: String
    val storeLink: String
    val type: Platform.Type

    enum class Type
    {
        IOS, ANDROID
    }

    fun shareFile(message: String, filePath: String)
    fun requestAuthorizationToSaveFiles()
    fun closeKeyBoard()
    fun copyToClipboard(text: String)
    fun showToast(message: String)
    fun requestReview(onDone : () -> Unit)
}

// expect fun getPlatform(): Platform
