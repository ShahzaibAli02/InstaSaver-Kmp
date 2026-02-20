package com.clipsaver.quickreels.domain.models

data class ScheduledPost(
        val id: String,
        val videoPath: String,
        val thumbnailPath: String,
        val scheduledTime: Long,
        val description: String,
        val hashtags: String,
        val status: String, // "READY", "DRAFT", "QUEUE"
        val isPosted: Boolean
)
