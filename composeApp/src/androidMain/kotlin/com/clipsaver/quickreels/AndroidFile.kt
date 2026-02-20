package com.clipsaver.quickreels

import com.clipsaver.quickreels.data.File
import java.io.FileOutputStream

class AndroidFile(private val filePath: String, append: Boolean = true) : File
{
    private val stream = FileOutputStream(filePath, append)

    override suspend fun write(bytes: ByteArray) {
        stream.write(bytes)
    }

    override suspend fun flush() {
        stream.flush()
    }

    override suspend fun close() {
        stream.close()
    }
}

actual  fun ReelFile(path : String) : File = AndroidFile(path)