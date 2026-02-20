package com.clipsaver.quickreels.data.local

import android.content.Context
import android.media.MediaScannerConnection
import java.io.File
import java.io.FileOutputStream

class FileManagerImpl(private val context: Context) : FileManager {
    override suspend fun saveFile(fileName: String, bytes: ByteArray): String? {
        return try {
            val directory = context.filesDir
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, fileName)
            FileOutputStream(file).use { outputStream -> outputStream.write(bytes) }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteFile(filePath: String): Boolean {
        return try {
            val file = File(filePath)
            file.exists() && file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun createFile(fileName: String, extension: String): String? {
        return try {
            val directory = context.externalMediaDirs.firstOrNull() ?: context.filesDir
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, "$fileName.$extension")
            if (!file.exists()) {
                file.createNewFile()
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun appendToFile(filePath: String, bytes: ByteArray) {
        try {
            val file = File(filePath)
            FileOutputStream(file, true).use { outputStream -> outputStream.write(bytes) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun exposeToGallery(filePath: String)
    {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath),
            null,
            null
        )
    }
}
