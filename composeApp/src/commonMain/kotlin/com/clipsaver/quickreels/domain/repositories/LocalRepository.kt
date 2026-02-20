package com.clipsaver.quickreels.domain.repositories

import com.clipsaver.quickreels.domain.models.Video
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun getRecentVideos(): Flow<List<Video>>
    suspend fun addVideo(video: Video)
    suspend fun updateVideo(video: Video)
    suspend fun deleteVideo(id: String)
    suspend fun getOpenCount(): Long
    suspend fun isRated(): Boolean
    suspend fun incrementOpenCount()
    suspend fun setRated()

    suspend fun isAutoPasteEnabled(): Boolean
    suspend fun toggleAutoPaste(enabled: Boolean)

    suspend fun isSaveToPhotosEnabled(): Boolean
    suspend fun toggleSaveToPhotos(enabled: Boolean)

    fun saveBaseUrl(url: String)
    fun getBaseUrl(): String

    suspend fun isTermsAccepted(): Boolean
    suspend fun setTermsAccepted()

    suspend fun getCredits(): Int
    suspend fun setCredits(credits: Int)
    suspend fun getTotalCredits(): Int
    suspend fun setTotalCredits(total: Int)
}
