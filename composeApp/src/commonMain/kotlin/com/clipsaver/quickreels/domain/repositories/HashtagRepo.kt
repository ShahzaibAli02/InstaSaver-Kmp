package com.clipsaver.quickreels.domain.repositories

interface HashtagRepo {
    fun saveTags(text: String)
    fun getTags(): String
}
