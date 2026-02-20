package com.clipsaver.quickreels

import com.clipsaver.quickreels.data.remote.DownloadState
import kotlinx.coroutines.flow.Flow

// androidMain/BackgroundDownloader.kt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

actual class BackgroundDownloader
{

    actual fun downloadFile(url: String, destinationPath: String): Flow<DownloadState> = flow {
        try
        {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK)
            {
                emit(DownloadState.Error("Server returned HTTP ${connection.responseCode}"))
                return@flow
            }

            val file = File(destinationPath)
            file.parentFile?.mkdirs()

            val input = connection.inputStream
            val output = FileOutputStream(file)
            val total = connection.contentLengthLong.coerceAtLeast(0L)
            var downloaded = 0L

            val buffer = ByteArray(32 * 1024)
            var bytesRead: Int

            while (input.read(buffer).also { bytesRead = it } != -1)
            {
                output.write(
                        buffer,
                        0,
                        bytesRead
                )
                downloaded += bytesRead

                if (total > 0)
                {
                    val progress = (downloaded / total.toFloat()).coerceIn(
                            0f,
                            1f
                    )
                    emit(DownloadState.Progress(progress)) // safe: runs on IO
                }
            }

            output.flush()
            output.close()
            input.close()
            connection.disconnect()

            emit(DownloadState.Success(destinationPath))
        } catch (e: Exception)
        {
            emit(DownloadState.Error("Download failed: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO) // Entire flow runs on IO
        .distinctUntilChanged() // Optional: avoid spamming same progress
        .conflate() // Very important: drop old progress if UI can't keep up

}