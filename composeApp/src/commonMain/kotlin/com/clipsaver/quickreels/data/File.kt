package com.clipsaver.quickreels.data

interface File {
    suspend fun write(bytes: ByteArray)
    suspend fun close()
    suspend fun flush() = Unit // optional
}

