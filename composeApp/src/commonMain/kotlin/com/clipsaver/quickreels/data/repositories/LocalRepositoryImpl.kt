package com.clipsaver.quickreels.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.clipsaver.quickreels.data.local.LocalSharedPref
import com.clipsaver.quickreels.db.AppDatabase
import com.clipsaver.quickreels.db.VideoEntity
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.domain.repositories.LocalRepository
import com.clipsaver.quickreels.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalRepositoryImpl(db: AppDatabase, sharedPref: LocalSharedPref) : LocalRepository {
        private val queries = db.videoQueries
        private val localSharedPref = sharedPref

        override fun getRecentVideos(): Flow<List<Video>> {
                return queries.getRecentVideos().asFlow().mapToList(Dispatchers.IO).map { entities
                        ->
                        entities.map { it.toVideo() }
                }
        }

        override suspend fun addVideo(video: Video) {
                queries.insertVideo(video.toEntity())
        }

        override suspend fun updateVideo(video: Video) {
                queries.updateVideo(video.toEntity())
        }

        override suspend fun deleteVideo(id: String) {
                queries.deleteVideo(id)
        }

        private fun VideoEntity.toVideo(): Video {
                return Video(
                        id = id,
                        name = name,
                        videoPath = videoPath,
                        thumbnailPath = thumbnailPath,
                        duration = duration,
                        timestamp = timestamp,
                        quality = quality,
                        isDownloading = isDownloading == 1L,
                        isDownloaded = isDownloaded == 1L,
                        progress = progress.toFloat(),
                        link = link,
                        isFailed = isFailed == 1L
                )
        }

        private fun Video.toEntity(): VideoEntity {
                return VideoEntity(
                        id = id,
                        name = name,
                        videoPath = videoPath,
                        thumbnailPath = thumbnailPath,
                        duration = duration,
                        timestamp = timestamp,
                        quality = quality,
                        isDownloading = if (isDownloading) 1L else 0L,
                        isDownloaded = if (isDownloaded) 1L else 0L,
                        progress = progress.toDouble(),
                        link = link,
                        isFailed = if (isFailed) 1L else 0L
                )
        }

        override suspend fun getOpenCount(): Long {

                return localSharedPref.getLong(Constants.KEYS.APP_OPEN_COUNT, 0)
        }

        override suspend fun isRated(): Boolean {
                return localSharedPref.getBool(Constants.KEYS.IS_RATED, false)
        }

        override suspend fun incrementOpenCount() {
                val prevCount = localSharedPref.getLong(Constants.KEYS.APP_OPEN_COUNT, 0)
                localSharedPref.saveLong(Constants.KEYS.APP_OPEN_COUNT, prevCount + 1)
        }

        override suspend fun setRated() {
                localSharedPref.saveBool(Constants.KEYS.IS_RATED, true)
        }

        override suspend fun isAutoPasteEnabled(): Boolean {
                return localSharedPref.getBool(Constants.KEYS.IS_AUTO_PASTE_ENABLED, true)
        }

        override suspend fun toggleAutoPaste(enabled: Boolean) {
                localSharedPref.saveBool(Constants.KEYS.IS_AUTO_PASTE_ENABLED, enabled)
        }

        override suspend fun isSaveToPhotosEnabled(): Boolean {
                return localSharedPref.getBool(Constants.KEYS.IS_SAVE_TO_PHOTOS_ENABLED, true)
        }

        override suspend fun toggleSaveToPhotos(enabled: Boolean) {
                localSharedPref.saveBool(Constants.KEYS.IS_SAVE_TO_PHOTOS_ENABLED, enabled)
        }

        override fun saveBaseUrl(url: String) {
                localSharedPref.saveString(Constants.KEYS.BASE_URL, url)
        }

        override fun getBaseUrl(): String {
                return localSharedPref.getString(
                        Constants.KEYS.BASE_URL,
                        Constants.DEFAULT_BASE_URL
                )
        }

        override suspend fun isTermsAccepted(): Boolean {
                return localSharedPref.getBool(Constants.KEYS.IS_TERMS_ACCEPTED, false)
        }

        override suspend fun setTermsAccepted() {
                localSharedPref.saveBool(Constants.KEYS.IS_TERMS_ACCEPTED, true)
        }

        override suspend fun getCredits(): Int {
                return localSharedPref
                        .getLong(
                                Constants.KEYS.REMAINING_CREDITS,
                                Constants.DEFAULT_TOTAL_CREDITS.toLong()
                        )
                        .toInt()
        }

        override suspend fun setCredits(credits: Int) {
                localSharedPref.saveLong(Constants.KEYS.REMAINING_CREDITS, credits.toLong())
        }

        override suspend fun getTotalCredits(): Int {
                return localSharedPref
                        .getLong(
                                Constants.KEYS.TOTAL_CREDITS,
                                Constants.DEFAULT_TOTAL_CREDITS.toLong()
                        )
                        .toInt()
        }

        override suspend fun setTotalCredits(total: Int) {
                localSharedPref.saveLong(Constants.KEYS.TOTAL_CREDITS, total.toLong())
        }
}
