package com.clipsaver.quickreels.ui.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class) @Composable
actual fun WebView(
    modifier: Modifier,
    url: String,
) {
    UIKitView(
        factory = {
            WKWebView()
        },
        modifier = modifier,
        update = { webView ->
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null) {
                webView.loadRequest(NSURLRequest.requestWithURL(nsUrl))
            }
        }
    )
}
