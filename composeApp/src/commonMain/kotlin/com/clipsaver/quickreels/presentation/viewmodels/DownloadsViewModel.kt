package com.clipsaver.quickreels.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clipsaver.quickreels.data.shared.EventType
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.domain.repositories.DownloadsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DownloadsViewModel(private val repository: DownloadsRepository) : ViewModel() {
        var showVideoPlayer by mutableStateOf(false)
        var videoPath by mutableStateOf("")
        var thumbnailPath by mutableStateOf("")
        val videos: StateFlow<List<Video>> =
                repository
                        .getRecentVideos()
                        .stateIn(
                                scope = viewModelScope,
                                started = SharingStarted.WhileSubscribed(5000),
                                initialValue = emptyList()
                        )

        fun cancelDownload(video: Video) =
                viewModelScope.launch {
                        //                println("cancelDownload() event Sent")
                        repository.sendEvent(EventType.CancelDownload(video))
                }
        fun deleteVideo(video: Video) {
                viewModelScope.launch { repository.sendEvent(EventType.DeleteVideo(video)) }
        }
        fun shareVideo(videoPath: String) {
                viewModelScope.launch { repository.sendEvent(EventType.ShareVideo(videoPath)) }
        }

        fun retryDownload(video: Video) =
                viewModelScope.launch { repository.sendEvent(EventType.RetryDownload(video)) }
}
