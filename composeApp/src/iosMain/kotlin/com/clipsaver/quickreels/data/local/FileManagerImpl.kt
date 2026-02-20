package com.clipsaver.quickreels.data.local

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.closeFile
import platform.Foundation.create
import platform.Foundation.fileHandleForWritingAtPath
import platform.Foundation.seekToEndOfFile
import platform.Foundation.writeToFile
import platform.Foundation.*
import platform.Photos.*
import platform.darwin.*

class FileManagerImpl : FileManager {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun saveFile(fileName: String, bytes: ByteArray): String? {
        return try {
            val fileManager = NSFileManager.defaultManager
            val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            val documentDirectory = urls.first() as? NSURL ?: return null
            val fileUrl = documentDirectory.URLByAppendingPathComponent(fileName) ?: return null

            val nsData =
                    bytes.usePinned { pinned ->
                        NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
                    }




            if (nsData.writeToFile(fileUrl.path!!, true)) {

                fileManager.setAttributes(
                        mapOf(NSFileProtectionKey to NSFileProtectionNone),
                        ofItemAtPath = fileUrl.path!!,
                        error = null
                )

                fileUrl.path
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun deleteFile(filePath: String): Boolean {
        return try {
            val fileManager = NSFileManager.defaultManager
            if (fileManager.fileExistsAtPath(filePath)) {
                fileManager.removeItemAtPath(filePath, null)
            } else {
                true // File does not exist, so deletion is considered successful.
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun createFile(fileName: String, extension: String): String? {
        return try {
            val fileManager = NSFileManager.defaultManager
            val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
            val documentDirectory = urls.first() as? NSURL ?: return null
            val fileUrl = documentDirectory.URLByAppendingPathComponent("$fileName.$extension")
                            ?: return null

            if (!fileManager.fileExistsAtPath(fileUrl.path!!)) {
                fileManager.createFileAtPath(fileUrl.path!!, null, null)
            }
            fileUrl.path
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun appendToFile(filePath: String, bytes: ByteArray) {
        try {
            val fileHandle = platform.Foundation.NSFileHandle.fileHandleForWritingAtPath(filePath)
            if (fileHandle != null) {
                fileHandle.seekToEndOfFile()
                val nsData = bytes.usePinned { pinned ->
                            NSData.create(
                                    bytes = pinned.addressOf(0),
                                    length = bytes.size.toULong()
                            )
                        }
                fileHandle.writeData(nsData,null)
                fileHandle.closeFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun exposeToGallery(filePath: String)
    {
        val url = NSURL.fileURLWithPath(filePath)

        val currentStatus = PHPhotoLibrary.authorizationStatus()

        fun saveVideo() {
            var placeholder: PHObjectPlaceholder? = null

            PHPhotoLibrary.sharedPhotoLibrary().performChanges({
                val request = PHAssetChangeRequest.creationRequestForAssetFromVideoAtFileURL(url)
                placeholder = request?.placeholderForCreatedAsset
            }) { success, error ->
                if (success && placeholder != null) {
                    // optional: implement album insertion in Swift/Kotlin
//                    callback(true, null)
                    println("SAVED TO GALLERY")
                } else {
                    println("FAILED TO  : SAVE TO GALLERY ${error?.localizedDescription}")
//                    callback(false, error?.localizedDescription)
                }
            }
        }

        when (currentStatus) {
            PHAuthorizationStatusAuthorized,
            PHAuthorizationStatusLimited -> saveVideo()

            PHAuthorizationStatusNotDetermined -> {
//                PHPhotoLibrary.requestAuthorization { status ->
//                    if (status == PHAuthorizationStatusAuthorized ||
//                        status == PHAuthorizationStatusLimited
//                    ) {
//                        saveVideo()
//                    } else {
//                        println("FAILED TO  : SAVE TO GALLERY : PERMISSION DENIED")
//                    }
//                }
            }

            else -> {
                println("FAILED TO  : SAVE TO GALLERY : PERMISSION DENIED")
            }
        }
    }
}
