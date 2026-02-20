package com.clipsaver.quickreels

import com.clipsaver.quickreels.data.remote.DownloadState
import kotlinx.coroutines.flow.Flow
expect class BackgroundDownloader(){
    fun downloadFile(
        url: String,
        destinationPath: String
    ): Flow<DownloadState>
}