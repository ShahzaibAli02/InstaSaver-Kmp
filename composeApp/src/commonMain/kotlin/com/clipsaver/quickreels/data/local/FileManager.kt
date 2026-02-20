package com.clipsaver.quickreels.data.local

interface FileManager {
    suspend fun saveFile(fileName: String, bytes: ByteArray): String?
    suspend fun deleteFile(filePath: String): Boolean
    suspend fun createFile(fileName: String, extension: String): String?
    suspend fun appendToFile(filePath: String, bytes: ByteArray)
    suspend fun exposeToGallery(filePath : String)
}

class FakeFileManager : FileManager {
    override suspend fun saveFile(fileName: String, bytes: ByteArray): String? {
        return "/fake/path/$fileName"
    }

    override suspend fun deleteFile(filePath: String): Boolean {
        return true
    }

    override suspend fun createFile(fileName: String, extension: String): String? {
        return "/fake/path/$fileName.$extension"
    }

    override suspend fun appendToFile(filePath: String, bytes: ByteArray) {
        // No-op for fake
    }

    override suspend fun exposeToGallery(filePath: String)
    {

    }
}
