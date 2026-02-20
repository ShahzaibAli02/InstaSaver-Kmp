package com.clipsaver.quickreels

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat

import android.content.Intent
import com.clipsaver.quickreels.utils.DeepLinkHelper
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    companion object{
        var activity : Activity? = null
    }
    
    // Inject DeepLinkHelper
    private val deepLinkHelper: DeepLinkHelper by inject()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        checkForPermission()
        activity = this@MainActivity
        
        // Handle deep link if activity is created from a share intent
        handleIntent(intent)
        
        setContent {
            val view = LocalView.current
            SideEffect {
                SetStatusBarStyle(view,true)
            }
            App()
        }
    }

    override fun onNewIntent(intent: Intent)
    {
        super.onNewIntent(intent)
        handleIntent(intent)
        // Ensure the new intent is set for future reference
        setIntent(intent)
    }


    private fun handleIntent(intent: Intent?) {
        intent?.let {
            if (it.action == Intent.ACTION_SEND && it.type == "text/plain") {
                val sharedText = it.getStringExtra(Intent.EXTRA_TEXT)
                if (!sharedText.isNullOrBlank()) {
                    deepLinkHelper.setSharedUrl(sharedText)
                }
            }
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        activity = null
    }

    fun checkForPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
            )
        }
    }
}

fun SetStatusBarStyle(view : View,darkIcons: Boolean) {

    if (!view.isInEditMode) {
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                it.insetsController?.setSystemBarsAppearance(
                        if (darkIcons)
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        else 0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                @Suppress("DEPRECATION")
                it.decorView.systemUiVisibility = if (darkIcons) {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    0
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}