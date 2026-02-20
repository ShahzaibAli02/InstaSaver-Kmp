package com.clipsaver.quickreels.data.remote

import com.clipsaver.quickreels.data.remote.models.TagsResponse
import com.clipsaver.quickreels.data.remote.models.VideoResponse
import kotlinx.coroutines.flow.Flow

interface NetworkHelper {
    suspend fun fetchVideo(url: String): Result<VideoResponse>
    suspend fun fetchHashTags(content: String): Result<TagsResponse>
    suspend fun downloadFile(url: String, destinationPath: String): Flow<DownloadState>
}

sealed class DownloadState {
    data object Idle : DownloadState()
    data class Progress(val progress: Float) : DownloadState()
    data class Success(val filePath: String) : DownloadState()
    data class Error(val message: String) : DownloadState()
}
