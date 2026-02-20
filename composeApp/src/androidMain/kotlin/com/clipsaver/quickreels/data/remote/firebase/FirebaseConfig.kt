package com.clipsaver.quickreels.data.remote.firebase

import com.clipsaver.quickreels.AppConfig
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class FirebaseConfig : AppConfig
{

    private val remoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
            fetchTimeoutInSeconds = 5
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun onConfig(onResponse: (Boolean, String) -> Unit) {
//        val appCheck = FirebaseAppCheck.AppCheck.appCheck()
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onResponse(true, "")
                    return@addOnCompleteListener
                }

                val json = remoteConfig.getString("app_config")
                if (json.isNullOrEmpty()) {
                    onResponse(true, "")
                } else {
                    onResponse(false, json)
                }
            }
    }
}