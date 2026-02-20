package com.clipsaver.quickreels

import com.clipsaver.quickreels.data.File
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.NSFileManager
import platform.Foundation.closeFile
import platform.Foundation.create
import platform.Foundation.fileHandleForWritingAtPath
import platform.Foundation.seekToEndOfFile

class IOSFile(private val filePath: String) : File
{
    private val fileHandle: NSFileHandle? = NSFileHandle.fileHandleForWritingAtPath(filePath)?.also {
        it.seekToEndOfFile()
    }

    @OptIn(ExperimentalForeignApi::class,
            BetaInteropApi::class
    ) override suspend fun write(bytes: ByteArray) {
        val fh = fileHandle ?: return
        val nsData = bytes.usePinned { pinned ->
            NSData.create(
                    bytes = pinned.addressOf(0),
                    length = bytes.size.toULong()
            )
        }
        fh.writeData(nsData,null)
    }

    override suspend fun close() {
        fileHandle?.closeFile()
    }
}

actual fun ReelFile(path : String) : File = IOSFile(path)
