package com.clipsaver.quickreels.utils

import com.clipsaver.quickreels.utils.ClipBoardHelper
import platform.UIKit.UIPasteboard

class ClipBoardHelperImpl : ClipBoardHelper
{
    override fun copyText(): String
    {
        return UIPasteboard.generalPasteboard.string ?: ""
    }

}