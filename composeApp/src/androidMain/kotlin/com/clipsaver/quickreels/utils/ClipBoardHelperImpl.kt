package com.clipsaver.quickreels.utils

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import com.clipsaver.quickreels.utils.ClipBoardHelper

class ClipBoardHelperImpl(private val context : Context): ClipBoardHelper
{
    override fun copyText(): String
    {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true)
        {
            return clipboard.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()
        }
        return null.orEmpty()
    }

}