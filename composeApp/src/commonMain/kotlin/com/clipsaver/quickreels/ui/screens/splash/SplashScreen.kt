package com.clipsaver.quickreels.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.clipsaver.quickreels.AppConfig
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.data.remote.models.BaseConfig
import com.clipsaver.quickreels.data.remote.models.ConfigConfirmAction
import com.clipsaver.quickreels.domain.repositories.LocalRepository
import com.clipsaver.quickreels.ui.components.BaseAccessDialog
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.common.AnalyticsHelper
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.utils.UpdateManager
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import reelsaverpro.composeapp.generated.resources.Res
import reelsaverpro.composeapp.generated.resources.appicon

@Composable fun SplashScreen(onNavigateToMain: () -> Unit)
{

    var baseConfig by remember { mutableStateOf<BaseConfig?>(null) }

    val localRepository : LocalRepository = getKoin().get()
    val platform: Platform = getKoin().get()
    val appConfig: AppConfig = getKoin().get()
    val analyticsHelper: AnalyticsHelper = getKoin().get()
    val uriHandler = LocalUriHandler.current

    // We simply want a loading state until we decide what to do
    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(modifier = Modifier.weight(1f))
            Image(
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(10.dp)),
                    painter = painterResource(Res.drawable.appicon), contentDescription = "App Icon")
            Spacer(modifier = Modifier.weight(1f))
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = PrimaryPurple,
                strokeWidth = 4.dp,
                trackColor = PrimaryPurple.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(50.dp))
        }

    }

    LaunchedEffect(Unit) {
        analyticsHelper.logScreenView(Strings.Analytics.Screens.Splash, Strings.Analytics.Screens.Splash)
        UpdateManager(appConfig).checkConfig(
                currentVersion = platform.version,
                onNavigateToMain = onNavigateToMain,
                onOtherConfig = { otherConfig ->
                    if(otherConfig.baseUrl?.isNotBlank() == true){
                        localRepository.saveBaseUrl(otherConfig.baseUrl.trim())
                    }
                },
                onShowDialog = { config ->
                    baseConfig = config
                })
    }


    if (baseConfig != null)
    {
        BaseAccessDialog(
                title = baseConfig?.title.orEmpty(),
                message = baseConfig?.message.orEmpty(),
                confirmText = baseConfig?.confirmText.orEmpty(),
                onConfirm = {

                    if (baseConfig?.action == ConfigConfirmAction.APP_UPDATE)
                    {
                        uriHandler.openUri(platform.storeLink)
                        return@BaseAccessDialog
                    }
                },
                dismissText = baseConfig?.dismissText.orEmpty(),
                onDismiss = {
                    baseConfig = null
                    onNavigateToMain()
                },
                cancellable = baseConfig?.cancelable ?: true
        )
    }
}
