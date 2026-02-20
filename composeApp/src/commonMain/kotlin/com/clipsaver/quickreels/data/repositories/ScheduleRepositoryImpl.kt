package com.clipsaver.quickreels.data.repositories

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.clipsaver.quickreels.db.AppDatabase
import com.clipsaver.quickreels.db.ScheduledVideo
import com.clipsaver.quickreels.domain.models.ScheduledPost
import com.clipsaver.quickreels.domain.repositories.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ScheduleRepositoryImpl(private val db: AppDatabase) : ScheduleRepository {

    private val queries = db.scheduledVideoQueries

    override fun getAllScheduledPosts(): Flow<List<ScheduledPost>> {
        return queries.getAllScheduledVideos().asFlow().mapToList(Dispatchers.IO).map { entities ->
            entities.map { entity ->
                ScheduledPost(
                        id = entity.id,
                        videoPath = entity.videoPath,
                        thumbnailPath = entity.thumbnailPath,
                        scheduledTime = entity.scheduledTime,
                        description = entity.description,
                        hashtags = entity.hashtags,
                        status = entity.status,
                        isPosted = entity.isPosted == 1L
                )
            }
        }
    }

    override suspend fun insertScheduledPost(post: ScheduledPost) {
        withContext(Dispatchers.IO) {
            queries.insertScheduledVideo(
                    ScheduledVideo(
                    id = post.id,
                    videoPath = post.videoPath,
                    thumbnailPath = post.thumbnailPath,
                    scheduledTime = post.scheduledTime,
                    description = post.description,
                    hashtags = post.hashtags,
                    status = post.status,
                    isPosted = if (post.isPosted) 1L else 0L
            ))
        }
    }

    override suspend fun deleteScheduledPost(id: String) {
        withContext(Dispatchers.IO) { queries.deleteScheduledVideo(id) }
    }

    override suspend fun getScheduledPostById(id: String): ScheduledPost? {
        return withContext(Dispatchers.IO) {
            val entity = queries.getScheduledVideoById(id).executeAsOneOrNull()
            entity?.let {
                ScheduledPost(
                        id = it.id,
                        videoPath = it.videoPath,
                        thumbnailPath = it.thumbnailPath,
                        scheduledTime = it.scheduledTime,
                        description = it.description,
                        hashtags = it.hashtags,
                        status = it.status,
                        isPosted = it.isPosted == 1L
                )
            }
        }
    }
}
