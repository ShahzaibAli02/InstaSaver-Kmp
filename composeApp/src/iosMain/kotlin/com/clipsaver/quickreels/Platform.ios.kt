package com.clipsaver.quickreels

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UIPasteboard
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.endEditing
import platform.UIKit.popoverPresentationController
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.NSEC_PER_SEC
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

class IOSPlatform : Platform
{
    override val version: String
        get()
        {
            val version = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
                ?: "N/A"
            return version
        }
    override val storeLink: String = Constants.STORE_LINK
    override val type: Platform.Type = Platform.Type.IOS
    override fun shareFile(message: String, filePath: String)
    {
        val url = NSURL.fileURLWithPath(filePath)
        if (url == null)
        {
            println("Invalid file path")
            return
        }

        // Prepare items to share
        val items = mutableListOf<Any>()
        if (message.isNotEmpty()) items.add(message)
        items.add(url)

        val controller = UIActivityViewController(
                activityItems = items,
                applicationActivities = null,
        )
        controller.excludedActivityTypes = listOf(
                platform.UIKit.UIActivityTypeSaveToCameraRoll
        ) // iPad support (mandatory)
        controller.popoverPresentationController?.sourceView = topViewController()?.view

        // Present share sheet
        topViewController()?.presentViewController(
                controller,
                animated = true,
                completion = null
        )
    }

    override fun requestAuthorizationToSaveFiles()
    {
        PHPhotoLibrary.requestAuthorization { status ->
            if (status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited)
            { //                                    saveVideo()
            } else
            {
                println("FAILED TO  : SAVE TO GALLERY : PERMISSION DENIED")
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class) override fun closeKeyBoard()
    {

        val sharedApp = UIApplication.sharedApplication // This hides the keyboard by asking the main window to end editing
        sharedApp.keyWindow?.endEditing(true)
    }

    override fun copyToClipboard(text: String)
    {
        UIPasteboard.generalPasteboard.string = text
    }

    override fun showToast(message: String)
    {
        val alert = UIAlertController.alertControllerWithTitle(
                title = null,
                message = message,
                preferredStyle = UIAlertControllerStyleAlert
        )

        topViewController()?.presentViewController(
                alert,
                animated = true,
                completion = null
        )

        // Dismiss after 1.5 seconds
        val delay = 1.5
        val time = dispatch_time(
                DISPATCH_TIME_NOW,
                (delay * NSEC_PER_SEC.toLong()).toLong()
        )
        dispatch_after(
                time,
                dispatch_get_main_queue()
        ) {
            alert.dismissViewControllerAnimated(
                    true,
                    completion = null
            )
        }
    }

    override fun requestReview(onDone : () -> Unit)
    {
        println("requestReview() iOS")
    }

    fun topViewController(
        controller: UIViewController? = UIApplication.sharedApplication.keyWindow?.rootViewController,
    ): UIViewController?
    {
        var c = controller
        while (true)
        {
            c = when
            {
                c is UINavigationController -> c.visibleViewController
                c is UITabBarController -> c.selectedViewController
                c?.presentedViewController != null -> c.presentedViewController
                else -> return c
            }
        }
    }

    override val name: String
        get()
        {
            val version = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
                ?: "N/A"
            val build = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleVersion") as? String
                ?: "N/A"
            return "v$version ($build)"
        }
}

// actual fun getPlatform(): Platform = IOSPlatform()
