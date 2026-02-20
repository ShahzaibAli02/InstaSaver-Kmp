package com.clipsaver.quickreels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.core.content.FileProvider
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import java.io.File

class AndroidPlatform(private val context: Context) : Platform
{
    override val version: String
        get()
        {
            return try
            {
                val packageInfo = context.packageManager.getPackageInfo(
                        context.packageName,
                        0
                )
                packageInfo.versionName.orEmpty()
            } catch (e: Exception)
            {
                "N/A"
            }
        }
    override val storeLink: String = Constants.STORE_LINK
    override val type: Platform.Type = Platform.Type.ANDROID
    override fun shareFile(message: String, filePath: String)
    {
        val file = File(filePath)
        val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "video/mp4" // Assuming it's always a video, adjust if needed
            putExtra(
                    Intent.EXTRA_STREAM,
                    uri
            )
            putExtra(
                    Intent.EXTRA_TEXT,
                    message
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(
                Intent.createChooser(
                        intent,
                        "Share video"
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // also safe here
                })
    }

    override fun requestAuthorizationToSaveFiles()
    {
    }

    override fun closeKeyBoard()
    {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val activity = context as? Activity
        val currentFocusedView = activity?.currentFocus
        if (currentFocusedView != null)
        {
            inputMethodManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken,
                    0
            )
        }
    }

    override fun copyToClipboard(text: String)
    {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText(
                "Copied Text",
                text
        )
        clipboard.setPrimaryClip(clip)
    }

    override fun showToast(message: String)
    {
        android.widget.Toast.makeText(
                context,
                message,
                android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    override fun requestReview(onDone : () -> Unit)
    {

//        val activity = context as? Activity
        MainActivity.activity?.let {
            println("requestReview()  shown")
            val manager = ReviewManagerFactory.create(MainActivity.activity!!)
            val request = manager.requestReviewFlow()

            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo: ReviewInfo = task.result

                    val flow = manager.launchReviewFlow(MainActivity.activity!!, reviewInfo)
                    flow.addOnCompleteListener {
                        onDone()
                        // Review flow finished
                    }
                }
            }
        }

    }

    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

// actual fun getPlatform(context: Context): Platform = AndroidPlatform(context)
