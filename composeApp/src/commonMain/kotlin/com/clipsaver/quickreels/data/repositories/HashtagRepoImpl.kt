package com.clipsaver.quickreels.data.repositories

import com.clipsaver.quickreels.data.local.LocalSharedPref
import com.clipsaver.quickreels.domain.repositories.HashtagRepo

class HashtagRepoImpl(private val localSharedPref: LocalSharedPref) : HashtagRepo {
    override fun saveTags(text: String) {
        localSharedPref.saveString("hashtag_history", text)
    }

    override fun getTags(): String {
        return localSharedPref.getString("hashtag_history", "[]")
    }
}
