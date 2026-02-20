package com.clipsaver.quickreels.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clipsaver.quickreels.domain.repositories.SettingsRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository): ViewModel() {

    var isAutoPasteEnabled by mutableStateOf(false)
        private set
    var isSaveToPhotosEnabled by mutableStateOf(false)
    private set

    init {
        viewModelScope.launch {
            isAutoPasteEnabled = repository.isAutoPasteEnabled()
            isSaveToPhotosEnabled = repository.isSaveToPhotosEnabled()
        }
    }
    fun toggleAutoPaste(enabled: Boolean) = viewModelScope.launch{
        isAutoPasteEnabled = enabled
        repository.toggleAutoPaste(enabled)
    }

    fun toggleSaveToPhotos(enabled: Boolean) = viewModelScope.launch{
        isSaveToPhotosEnabled = enabled
        repository.toggleSaveToPhotos(enabled)
    }
}
