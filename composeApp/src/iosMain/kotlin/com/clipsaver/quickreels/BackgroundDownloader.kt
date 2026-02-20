package com.clipsaver.quickreels // iosMain/BackgroundDownloader.kt
import com.clipsaver.quickreels.data.remote.DownloadState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Foundation.*
import platform.darwin.*
import platform.posix.int64_t
import kotlin.native.concurrent.*

actual class BackgroundDownloader {

    @OptIn(ExperimentalForeignApi::class) actual fun downloadFile(
        url: String,
        destinationPath: String
    ): Flow<DownloadState> = callbackFlow {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl == null) {
            trySend(DownloadState.Error("Invalid URL"))
            close()
            return@callbackFlow
        }

        // Create background session
        val identifier = "com.app.download.${destinationPath.hashCode()}"
        val config = NSURLSessionConfiguration.backgroundSessionConfigurationWithIdentifier(identifier)
        config.allowsCellularAccess = true
        config.sessionSendsLaunchEvents = true

        val session = NSURLSession.sessionWithConfiguration(config, delegate = object : NSObject(), NSURLSessionDownloadDelegateProtocol {


            override fun URLSession(
                session: NSURLSession,
                downloadTask: NSURLSessionDownloadTask,
                didWriteData: int64_t,
                totalBytesWritten: int64_t,
                totalBytesExpectedToWrite: int64_t
            )
            {
                val progress = if (totalBytesExpectedToWrite > 0L) {
                    totalBytesWritten.toFloat() / totalBytesExpectedToWrite
                } else 0f
                trySend(DownloadState.Progress(progress))
            }



            override fun URLSession(
                session: NSURLSession,
                downloadTask: NSURLSessionDownloadTask,
                didFinishDownloadingToURL: NSURL
            ) {

                val fileManager = NSFileManager.defaultManager
                val destUrl = NSURL.fileURLWithPath(destinationPath)

                // Ensure directory exists
                val destDir = destUrl.URLByDeletingLastPathComponent
                if (!fileManager.fileExistsAtPath(destDir?.path ?: "")) {
                    fileManager.createDirectoryAtPath(destDir?.path ?: "", withIntermediateDirectories = true, attributes = null, error = null)
                }

                // Remove existing file if present
                if (fileManager.fileExistsAtPath(destUrl.path!!)) {
                    fileManager.removeItemAtURL(destUrl, null)
                }

                // Copy and delete original only if copy succeeds
                val copySuccess = fileManager.copyItemAtURL(didFinishDownloadingToURL, destUrl, null)
                if (copySuccess) {
                    fileManager.removeItemAtURL(didFinishDownloadingToURL, null)
                    fileManager.setAttributes(
                            mapOf(NSFileProtectionKey to NSFileProtectionNone),
                            ofItemAtPath = destUrl.path!!,
                            error = null
                    )

                    trySend(DownloadState.Success(destinationPath))
                } else {
                    trySend(DownloadState.Error("Failed to copy file"))
                }


                close()
            }

            override fun URLSession(session: NSURLSession, task: NSURLSessionTask, didCompleteWithError: NSError?) {
                if (didCompleteWithError != null) {
                    trySend(DownloadState.Error(didCompleteWithError.localizedDescription))
                    close()
                }
            }

        }, delegateQueue = NSOperationQueue.mainQueue)

        val task = session.downloadTaskWithURL(nsUrl)
        task.resume()

        awaitClose {
            // cancel download if Flow collector disappears
            task.cancel()
        }
    }
}
