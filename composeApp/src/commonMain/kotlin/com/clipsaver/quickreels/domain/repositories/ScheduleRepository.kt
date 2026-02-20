package com.clipsaver.quickreels.domain.repositories

import com.clipsaver.quickreels.domain.models.ScheduledPost
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getAllScheduledPosts(): Flow<List<ScheduledPost>>
    suspend fun insertScheduledPost(post: ScheduledPost)
    suspend fun deleteScheduledPost(id: String)
    suspend fun getScheduledPostById(id: String): ScheduledPost?
}
