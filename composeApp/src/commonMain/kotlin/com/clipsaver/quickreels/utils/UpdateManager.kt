package com.clipsaver.quickreels.utils

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import com.clipsaver.quickreels.AppConfig
import com.clipsaver.quickreels.data.remote.models.BaseConfig
import com.clipsaver.quickreels.data.remote.models.MaintenanceConfig
import com.clipsaver.quickreels.data.remote.models.OtherConfig
import com.clipsaver.quickreels.data.remote.models.RemoteConfigResponse
import com.clipsaver.quickreels.data.remote.models.UpdateConfig
import kotlinx.serialization.json.Json

class UpdateManager(private val remoteConfig: AppConfig)
{

    fun checkConfig(
        currentVersion: String,
        onNavigateToMain: () -> Unit,
        onOtherConfig: (OtherConfig) -> Unit,
        onShowDialog: (BaseConfig) -> Unit,
    )
    {
        println("checkConfig()")
        remoteConfig.onConfig { error, response ->
            println("checkConfig() error = $error response = $response")
            if (error)
            { // On error, proceed to main app
                println("errorerror()")
                onNavigateToMain()
            } else
            {
                try
                {
                    val jsonParser = Json { ignoreUnknownKeys = true }
                    val config = jsonParser.decodeFromString<RemoteConfigResponse>(response)

                    if (config.otherConfig != null)
                    {
                        onOtherConfig(config.otherConfig)
                    }

                    when
                    {
                        config.maintenanceMode?.enabled == true && config.maintenanceMode.versions != null && currentVersionInList(
                                currentVersion,
                                config.maintenanceMode.versions
                        ) ->
                        {
                            onShowDialog(config.maintenanceMode.toBaseConfig())
                        }

                        config.forceUpdate?.enabled == true && shouldUpdate(
                                config.forceUpdate.version,
                                currentVersion
                        ) ->
                        {
                            onShowDialog(
                                    config.forceUpdate.toBaseConfig(
                                            isCancellable = false,
                                            currentVersion = currentVersion
                                    )
                            )
                        }

                        config.optionalUpdate?.enabled == true && shouldUpdate(
                                config.optionalUpdate.version,
                                currentVersion
                        ) ->
                        {
                            onShowDialog(
                                    config.optionalUpdate.toBaseConfig(
                                            isCancellable = true,
                                            currentVersion = currentVersion
                                    )
                            )
                        }

                        else ->
                        {
                            println("onNavigateToMain() ELSE")
                            onNavigateToMain()
                        }
                    }
                } catch (e: Exception)
                {
                    println("JSON Parse Error: ${e.message}")
                    onNavigateToMain()
                }
            }
        }
    }

    private fun currentVersionInList(currentVersion: String, versions: List<String>): Boolean
    {
        return versions.any { it.trim() == currentVersion.trim() || it.trim().lowercase() == "all" }
    }

    private fun shouldUpdate(configVersion: String?, appVersion: String): Boolean
    {
        if (configVersion.isNullOrBlank()) return false

        return isGreeter(
                newVersion = configVersion,
                oldVersion = appVersion
        )
    }

    private fun isGreeter(newVersion: String, oldVersion: String): Boolean
    {

        fun normalize(v: String): Int
        {
            val parts = v.split(".")
            val major = parts.getOrNull(0)?.toIntOrNull() ?: 0
            val minor = parts.getOrNull(1)?.toIntOrNull() ?: 0
            val patch = parts.getOrNull(2)?.toIntOrNull() ?: 0

            // pad each to 3 digits → 000000000–999999999
            return major * 1_000_000 + minor * 1_000 + patch
        }

        val version1 = normalize(newVersion)
        val version2 = normalize(oldVersion)

        return version1 > version2
    }

}