package com.clipsaver.quickreels.data.remote.models

import com.clipsaver.quickreels.utils.Util
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
        val url: String? = null,
        val source: String? = null,
        val id: String? = null,
        @SerialName("unique_id") val uniqueId: String? = null,
        val author: String? = null,
        val title: String? = null,
        val thumbnail: String? = null,
        val duration: Double? = null,
        val medias: List<Media>? = null,
        val type: String? = null,
        val error: Boolean? = null,
        val message: String? = null,
) {
    fun getMedia(): List<Media> {
        return medias
                ?.filter { it.type != "audio" }
                ?.map { it.copy(quality = Util.extractVideoQuality(it.quality.orEmpty())) }
                ?.distinctBy { it.quality }
                ?.sortedBy { extractQualityNumber(it.quality.orEmpty()) } // <-- SORT HERE
         ?: emptyList()
    }

    private fun extractQualityNumber(q: String): Int {
        return q.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
}

@Serializable
data class Media(
        val url: String? = null,
        @SerialName("data_size") val dataSize: Long? = null,
        val quality: String? = null,
        val extension: String? = null,
        val type: String? = null,
//    val duration: Double? = null
)
@Serializable
data class Tag(
    val tag : String? = null,
    val popularity: Long? = null
)
@Serializable
data class TagsResponse(
    val tags: List<Tag>? = null,
    val error: Boolean? = null,
    val message: String? = null,
)
