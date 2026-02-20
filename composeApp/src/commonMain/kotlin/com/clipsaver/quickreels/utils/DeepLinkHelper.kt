package com.clipsaver.quickreels.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DeepLinkHelper {
    private val _sharedUrl = MutableStateFlow<String?>(null)
    val sharedUrl = _sharedUrl.asStateFlow()

    fun setSharedUrl(url: String) {
        _sharedUrl.value = url
    }

    fun clearSharedUrl() {
        _sharedUrl.value = null
    }
}
