package com.clipsaver.quickreels.domain.models

data class Video(
        val id: String,
        val name: String,
        val videoPath: String,
        val thumbnailPath: String,
        val duration: String,
        val timestamp: Long,
        val quality: String = "HD",
        var isDownloading: Boolean = false,
        val isDownloaded: Boolean = true,
        val progress: Float = 0f,
        val link: String = "",
        var isFailed: Boolean = false
)
