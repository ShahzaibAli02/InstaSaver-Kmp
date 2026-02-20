package com.clipsaver.quickreels.domain.repositories

import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.data.local.FileManager
import com.clipsaver.quickreels.data.remote.NetworkHelper
import com.clipsaver.quickreels.data.shared.Events
import com.clipsaver.quickreels.domain.models.Video

interface HomeRepository : Events, Platform,  FileManager, LocalRepository , NetworkHelper {
}
