package com.clipsaver.quickreels.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteConfigResponse(
        @SerialName("force_update") val forceUpdate: UpdateConfig? = null,
        @SerialName("optional_update") val optionalUpdate: UpdateConfig? = null,
        @SerialName("maintenance_mode") val maintenanceMode: MaintenanceConfig? = null,
        @SerialName("others") val otherConfig: OtherConfig? = null
)

@Serializable
data class UpdateConfig(
        val enabled: Boolean = false,
        val version: String? = null,
        val title: String? = null,
        val message: String? = null
) {
        fun toBaseConfig(currentVersion: String, isCancellable: Boolean): BaseConfig {
                val extraMessage = "Version $version is now available. Your installed version is $currentVersion."
                return BaseConfig(
                        title = title ?: "Update Required",
                        message = message.orEmpty() + extraMessage,
                        cancelable = isCancellable,
                        confirmText = "Update Now",
                        dismissText = "Later",
                        action = ConfigConfirmAction.APP_UPDATE
                )
        }
}

@Serializable data class OtherConfig(@SerialName("base_url") val baseUrl: String? = null)

@Serializable
data class MaintenanceConfig(
        val enabled: Boolean = false,
        val versions: List<String>? = null,
        val title: String? = null,
        val message: String? = null
) {
        fun toBaseConfig(isCancellable: Boolean = false): BaseConfig {

                return BaseConfig(
                        title = title ?: "Server Maintenance",
                        message = message
                                        ?: "Our servers are currently down. Please try again later.",
                        cancelable = isCancellable,
                        confirmText = "OK",
                        dismissText = "Cancel",
                        action = ConfigConfirmAction.NOTHING
                )
        }
}

enum class ConfigConfirmAction {
        APP_UPDATE,
        NOTHING
}

@Serializable
data class BaseConfig(
        val title: String,
        val message: String,
        val cancelable: Boolean,
        val confirmText: String,
        val action: ConfigConfirmAction,
        val dismissText: String
)
