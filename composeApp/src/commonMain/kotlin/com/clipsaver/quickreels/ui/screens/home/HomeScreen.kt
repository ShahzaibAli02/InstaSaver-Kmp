package com.clipsaver.quickreels.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.clipsaver.quickreels.ads.Ads
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.presentation.viewmodels.HomeViewModel
import com.clipsaver.quickreels.presentation.viewmodels.SubscriptionViewModel
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.components.DownloadVideoItem
import com.clipsaver.quickreels.ui.components.FetchResultPopup
import com.clipsaver.quickreels.ui.components.HeaderTitle
import com.clipsaver.quickreels.ui.components.HowToUseDialog
import com.clipsaver.quickreels.ui.components.PreparingPopup
import com.clipsaver.quickreels.ui.components.VideoPlayerDialog
import com.clipsaver.quickreels.ui.theme.AccentPinkGradient
import com.clipsaver.quickreels.ui.theme.AccentPinkStart
import com.clipsaver.quickreels.ui.theme.BackgroundOffWhite
import com.clipsaver.quickreels.ui.theme.DarkTextCharcoal
import com.clipsaver.quickreels.ui.theme.LightTextGray
import com.clipsaver.quickreels.ui.theme.PrimaryPurple
import com.clipsaver.quickreels.ui.theme.SurfaceWhite
import com.clipsaver.quickreels.utils.ClipBoardHelper
import com.clipsaver.quickreels.common.AnalyticsHelper
import com.clipsaver.quickreels.data.remote.models.Media
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import reelsaverpro.composeapp.generated.resources.Res
import reelsaverpro.composeapp.generated.resources.ic_crown
import reelsaverpro.composeapp.generated.resources.ic_premium

@Composable fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    isPremium : Boolean,
    onGoPremium : () -> Unit,
    onNavigateDownloads: () -> Unit,
    onShowAd : (adType: Ads.TYPE,onDone : () -> Unit) -> Unit,
    isAdReady : (adType: Ads.TYPE) -> Boolean,
    onLoadAd : (adType: Ads.TYPE,onDone : (Boolean) -> Unit) -> Unit,
    )
{

    val recentVideos by viewModel.recentVideos.collectAsState()
    val videoResponse by viewModel.videoResponse.collectAsState()
    val error by viewModel.error.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val processingProgress by viewModel.processingProgress.collectAsState()
    val sharedUrl by viewModel.sharedUrl.collectAsState()
    val clipboardHelper: ClipBoardHelper = getKoin().get()
    val analyticsHelper: AnalyticsHelper = getKoin().get()

    var urlText by remember { mutableStateOf("") }
    
    LaunchedEffect(sharedUrl){
        if(!sharedUrl.isNullOrBlank()){
            urlText = sharedUrl ?: ""
            viewModel.fetchVideo(urlText)
            analyticsHelper.logEvent(
                Strings.Analytics.Events.FetchVideo,
                mapOf(Strings.Analytics.Params.Url to urlText)
            )
            viewModel.clearSharedUrl()
        }
    }
    var showHowToUseDialog by remember { mutableStateOf(false) } //    var isConsentGiven by remember { mutableStateOf(false) } //    var showConsentError by remember { mutableStateOf(false) }

    fun downloadVideo(media: Media)
    {

        viewModel.downloadVideo(
                url = media.url.orEmpty(),
                extension = media.extension.orEmpty(),
                title = videoResponse?.title.orEmpty(),
                thumbnail = videoResponse?.thumbnail.orEmpty(),
                duration = videoResponse?.duration ?: 0.0,
                actualLink = videoResponse?.url.orEmpty(),
                quality = media.quality
                    ?: "HD", //                                        isPremium =
                // isPremium
                isPremium = true
        )
        analyticsHelper.logEvent(
                Strings.Analytics.Events.DownloadVideo,
                mapOf(Strings.Analytics.Params.VideoPath to videoResponse?.url.orEmpty())
        )
        viewModel.clearVideoResponse()
    }

    fun loadAdAndWait(adType :Ads.TYPE = Ads.TYPE.REWARDED,onDone: () -> Unit){
        viewModel.isLoading = true
        println("HomeScreen() ->loadAdAndWait ")
        onLoadAd(adType,{
            println("HomeScreen() -> loadAdAndWait  ${it}")
            viewModel.isLoading = false
            onDone()
        })

    }


    fun showAdThenProceed(adType: Ads.TYPE,action : () -> Unit){
        if(isAdReady(adType)){
            onShowAd(adType){
                println("HomeScreen() -> onShowAd() DONE")
                action()
            }
            return
        }
        action()
    }
    fun onDownloadClick(media: Media)
    {

        if(isPremium){
            downloadVideo(media)
            return
        }
        if(isAdReady(Ads.TYPE.REWARDED)){
            showAdThenProceed(Ads.TYPE.REWARDED, action = {
                downloadVideo(media)
            })
            return
        }

        loadAdAndWait(Ads.TYPE.REWARDED,{
          showAdThenProceed(Ads.TYPE.REWARDED, action = {downloadVideo(media)})
        })
    }

    fun onVideoClick(video : Video){


        fun action() {
            println("HomeScreen() -> action()")
            viewModel.showVideoPlayer = true
            viewModel.videoPath = video.videoPath
            viewModel.thumbnailPath = video.thumbnailPath
        }



        //REMOVED INTERSTITIAL AD
        action()


//        if(isPremium){
//            action()
//            return
//        }


//        if(isAdReady(Ads.TYPE.INTERSTITIAL)){
//            showAdThenProceed(Ads.TYPE.INTERSTITIAL, action = ::action)
//            return
//        }
//
//        loadAdAndWait(Ads.TYPE.INTERSTITIAL,{
//            showAdThenProceed(Ads.TYPE.INTERSTITIAL, action = ::action)
//        })
    }

    LaunchedEffect(Unit) { viewModel.receiveEvents() }

    LaunchedEffect(Unit) {
        if (!viewModel.isFetchedRecentVideos)
        {
            analyticsHelper.logScreenView(
                    Strings.Analytics.Screens.Home,
                    Strings.Analytics.Screens.Home
            )
            viewModel.fetchRecentVideos()
            viewModel.checkDownloadStatus()
        }
    }

    if (viewModel.showVideoPlayer)
    {
        VideoPlayerDialog(
                videoPath = viewModel.videoPath,
                thumbnailPath = viewModel.thumbnailPath,
                modifier = Modifier,
                onDismiss = {
                    viewModel.showVideoPlayer = false
                    viewModel.videoPath = ""
                },
                onShareClick = {
                    viewModel.shareVideo(
                            viewModel.videoPath,
                    )
                    analyticsHelper.logEvent(Strings.Analytics.Events.ShareVideo)
                })
    }
    if (isProcessing)
    {
        PreparingPopup(
                progress = processingProgress,
                onCancel = { viewModel.cancelProcessing() })
    }

    if (viewModel.isLoading)
    {
        Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                )
        ) {
            Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp).background(
                            SurfaceWhite,
                            shape = RoundedCornerShape(8.dp)
                    )
            ) { CircularProgressIndicator(color = PrimaryPurple) }
        }
    }

    if (error != null)
    {
        AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                title = { Text("Error") },
                text = { Text(error ?: "Unknown error") },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text(
                                "OK",
                                color = PrimaryPurple
                        )
                    }
                },
                containerColor = SurfaceWhite,
                titleContentColor = DarkTextCharcoal,
                textContentColor = LightTextGray
        )
    }

    if (videoResponse != null)
    {
        FetchResultPopup(
                videoResponse = videoResponse!!,
                onDismiss = { viewModel.clearVideoResponse() },
                onDownload = { media ->

                    onDownloadClick(media)




                }, //                        isPremium = isPremium,
                isPremium = true,
                onLockedItemClick = { viewModel.showPremiumPayWall() })
    }

    if (showHowToUseDialog)
    {
        HowToUseDialog(onDismiss = { showHowToUseDialog = false })
    }

    //        if (showCreditsInfoDialog) {
    //                AlertDialog(
    //                        onDismissRequest = { showCreditsInfoDialog = false },
    //                        title = { Text("Credit System", fontWeight = FontWeight.Bold) },
    //                        text = {
    //                                Text(
    //                                        "You have restricted downloads as a free
    // user.\n\n" +
    //                                                "• Each video download costs 1 credit.\n"
    // +
    //                                                "• Provide credits are reset periodically
    // (mock logic).\n\n" +
    //                                                "Upgrade to Premium for unlimited
    // downloads!"
    //                                )
    //                        },
    //                        confirmButton = {
    //                                TextButton(onClick = { showCreditsInfoDialog = false }) {
    //                                        Text("Got it", color = PrimaryPurple)
    //                                }
    //                        },
    //                        containerColor = SurfaceWhite,
    //                        titleContentColor = DarkTextCharcoal,
    //                        textContentColor = DarkTextCharcoal
    //                )
    //        }

    //        if (showPremiumPaywall) {
    //                // Mock Paywall or Dialog for now, user asked to just call method
    //                // showPremiumPayWall()
    //                // But we probably want to show something. For now using a simple Alert.
    //                // Or maybe user didn't ask for UI, just the logic.
    //                // "force user to buy premium by showing premium wall"
    //                // I will create a simple Dialog for now stating "Premium Required"
    //                AlertDialog(
    //                        onDismissRequest = { viewModel.dismissPremiumPayWall() },
    //                        title = { Text("Premium Required") },
    //                        text = {
    //                                Text(
    //                                        "You are out of credits or trying to access a
    // premium feature. Upgrade to Premium!"
    //                                )
    //                        },
    //                        confirmButton = {
    //                                TextButton(
    //                                        onClick = { viewModel.dismissPremiumPayWall() }
    //                                ) { // TODO: Navigate to premium
    //                                        Text("Upgrade", color = PrimaryPurple)
    //                                }
    //                        },
    //                        dismissButton = {
    //                                TextButton(onClick = { viewModel.dismissPremiumPayWall()
    // }) {
    //                                        Text("Cancel", color = LightTextGray)
    //                                }
    //                        },
    //                        containerColor = SurfaceWhite
    //                )
    //        }

    Scaffold(
            topBar = {

                Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 30.dp)
                            .padding(bottom = 24.dp)
                ){
                    HeaderTitle(modifier = Modifier,title = Strings.Home)
                    Spacer(modifier = Modifier.weight(1f))
                    if(!isPremium){
                        GoPremiumButton({
                            onGoPremium()
                        })
                    }
                }



            },
            content = { paddingValues ->
                Column(
                        modifier = Modifier.fillMaxSize().background(BackgroundOffWhite)
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    // How to use & Credits Row
                    Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) { // How to Use (Left)
                        Row(
                                modifier = Modifier.clickable { showHowToUseDialog = true },
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                    text = Strings.HowToUse,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = PrimaryPurple
                                    )
                            )
                        }

                        // Credits (Right)
                        //                        if (!isPremium) {
                        //                                Card(
                        //                                        colors =
                        //                                                CardDefaults.cardColors(
                        //                                                        containerColor =
                        // SurfaceWhite
                        //                                                ),
                        //                                        shape = RoundedCornerShape(8.dp),
                        //                                        elevation =
                        //
                        // CardDefaults.cardElevation(defaultElevation = 2.dp),
                        //                                        modifier =
                        //                                                Modifier.clickable {
                        // showCreditsInfoDialog = true }
                        //                                ) {
                        //                                        Row(
                        //                                                modifier =
                        //                                                        Modifier.padding(
                        //                                                                horizontal
                        // = 12.dp,
                        //                                                                vertical =
                        // 6.dp
                        //                                                        ),
                        //                                                verticalAlignment =
                        // Alignment.CenterVertically
                        //                                        ) {
                        //                                                Text(
                        //                                                        text = "Credits:
                        // $credits / $totalCredits",
                        //                                                        style =
                        //
                        // MaterialTheme.typography.bodyMedium
                        //
                        // .copy(
                        //
                        //      fontWeight =
                        //
                        //              FontWeight
                        //
                        //                      .Bold,
                        //
                        //      color =
                        //
                        //              if (credits ==
                        //
                        //                              0
                        //
                        //              )
                        //
                        //                      Color.Red
                        //
                        //              else
                        //
                        //                      PrimaryPurple
                        //                                                                        )
                        //                                                )
                        //                                        }
                        //                                }
                        //                        }
                    }

                    // Input Card
                    Card(
                            modifier = Modifier.fillMaxWidth().padding(2.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) { // Text Field Row
                            Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                        value = urlText,
                                        onValueChange = { urlText = it },
                                        placeholder = {
                                            Text(
                                                    Strings.PasteLinkPlaceholder,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = LightTextGray
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        shape = RoundedCornerShape(8.dp),
                                        trailingIcon = {
                                            Icon(
                                                    Icons.Filled.Link,
                                                    contentDescription = Strings.LinkIconDescription,
                                                    tint = PrimaryPurple
                                            )
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = PrimaryPurple,
                                                unfocusedBorderColor = LightTextGray,
                                                cursorColor = PrimaryPurple,
                                        ),
                                        singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                if (urlText.isNotBlank())
                                {
                                    IconButton(
                                            onClick = {
                                                urlText = ""
                                                analyticsHelper.logEvent(Strings.Analytics.Events.ClearLink)
                                            },
                                            modifier = Modifier.background(
                                                    Color.Black.copy(
                                                            alpha = 0.6f
                                                    ),
                                                    CircleShape
                                            ).size(
                                                    40.dp
                                            ) // Adjust size to match
                                            // button height roughly
                                    ) {
                                        Icon(
                                                Icons.Default.Close,
                                                contentDescription = Strings.Clear,
                                                tint = Color.White
                                        )
                                    }
                                } else
                                {
                                    Button(
                                            onClick = {
                                                urlText = clipboardHelper.copyText()
                                                analyticsHelper.logEvent(Strings.Analytics.Events.PasteLink)
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                    containerColor = AccentPinkStart
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(
                                                    horizontal = 16.dp,
                                                    vertical = 16.dp
                                            )
                                    ) {
                                        Text(
                                                Strings.PasteButton,
                                                color = Color.White
                                        )
                                    }
                                }


                                // Button(onClick = {
                                // if(urlText.isBlank()){
                                // }
                                // else{
                                // urlText = ""
                                // }
                                // },
                                // colors =
                                // ButtonDefaults.buttonColors(
                                // containerColor = if(urlText.isBlank()) AccentPinkStart else RED
                                // ),
                                // shape = RoundedCornerShape(8.dp),
                                // contentPadding =
                                // PaddingValues(
                                // horizontal = 16.dp,
                                // vertical = 16.dp
                                // )
                                // ) { Text(if(urlText.isBlank()) Strings.PasteButton else Strings.Clear, color = Color.White) }
                            }

                            //                            if (urlText.isNotBlank())
                            //                            {
                            //                                Spacer(modifier = Modifier.height(16.dp))
                            //
                            //                                Row(
                            //                                        modifier = Modifier.fillMaxWidth().clickable {
                            //                                            isConsentGiven = !isConsentGiven
                            //                                        },
                            //                                        verticalAlignment = Alignment.CenterVertically
                            //                                ) {
                            //                                    Checkbox(
                            //                                            checked = isConsentGiven,
                            //                                            onCheckedChange = { isConsentGiven = it },
                            //                                            colors = CheckboxDefaults.colors(
                            //                                                    checkedColor = PrimaryPurple,
                            //                                                    uncheckedColor = LightTextGray,
                            //                                                    checkmarkColor = Color.White
                            //                                            )
                            //                                    )
                            //                                    Spacer(modifier = Modifier.width(2.dp))
                            //                                    Text(
                            //                                            text = "I confirm that I own this content or have permission from the content owner",
                            //                                            style = MaterialTheme.typography.labelMedium,
                            //                                            color = DarkTextCharcoal
                            //                                    )
                            //                                }
                            //                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Fetch Button with Gradient
                            Box(modifier = Modifier.fillMaxWidth().alpha(
                                            if (urlText.isNotBlank()) 1f
                                            else 0.5f
                                    ) // Disable))
                                        .height(40.dp).clip(RoundedCornerShape(8.dp)).background(
                                                Brush.horizontalGradient(
                                                        AccentPinkGradient
                                                )
                                        ).clickable(
                                                enabled = urlText.isNotBlank(),
                                                onClick ={
                                                    viewModel.fetchVideo(urlText)
                                                    analyticsHelper.logEvent(
                                                            Strings.Analytics.Events.FetchVideo,
                                                            mapOf(Strings.Analytics.Params.Url to urlText)
                                                    )
                                                }
                                        ), // transparent
                                    // background
                                    contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                            text = Strings.FetchVideoButton,
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                text = Strings.RecentDownloadsHeader,
                                style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkTextCharcoal,
                                ),
                        )
                        TextButton(onClick = {
                            onNavigateDownloads()
                            analyticsHelper.logEvent(Strings.Analytics.Events.ViewAllRecent)
                        }) {
                            Text(
                                    text = "View All",
                                    color = PrimaryPurple,
                                    fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (recentVideos.isEmpty())
                    {
                        Box(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                        ) {
                            Text(
                                    text = Strings.NoRecentVideos,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LightTextGray
                            )
                        }
                    } else
                    {
                        val sortedVideos = recentVideos.sortedWith(compareByDescending<Video> {
                            it.isDownloading || it.isFailed
                        }.thenByDescending { it.timestamp })

                        LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                        ) {

                            items(sortedVideos) { video ->
                                DownloadVideoItem(
                                        video = video,
                                        onVideoClick = {
                                            onVideoClick(video)
                                        },
                                        onDeleteClick = { viewModel.deleteVideo(video) },
                                        onShareClick = {
                                            viewModel.shareVideo(video.videoPath)
                                            analyticsHelper.logEvent(Strings.Analytics.Events.ShareVideo)
                                        },
                                        onCancelDownload = {
                                            viewModel.cancelDownload(video)
                                        },
                                        onRetryClick = { viewModel.retryDownload(video) })
                            }
                        }
                    }
                }
            })
}



@Composable fun QualityChip(
    modifier: Modifier = Modifier,
    type: String?,
    quality: String,
    selected: Boolean,
    locked: Boolean = false,
)
{
    Box(
            modifier = modifier.height(50.dp).clip(RoundedCornerShape(8.dp))
                .background(if (selected) PrimaryPurple else Color.Transparent).border(
                        1.dp,
                        if (selected) PrimaryPurple else LightTextGray,
                        RoundedCornerShape(8.dp)
                ).padding(horizontal = 15.dp)
    ) {
        if (locked)
        {
            Image(
                    modifier = Modifier.padding(top = 5.dp).size(12.dp).align(
                            Alignment.TopEnd
                    ), // Center lock if locked, or adjust as needed
                    painter = painterResource(
                            Res.drawable.ic_premium
                    ), // Need to import or use fully qualified if not imported
                    contentDescription = "Locked"
            )
        } else if (type != null)
        {
            Icon(
                    modifier = Modifier.padding(top = 5.dp).size(15.dp).align(Alignment.TopEnd),
                    imageVector = if (type == "audio") Icons.Default.AudioFile
                    else Icons.Default.Videocam,
                    contentDescription = "",
                    tint = if (selected) Color.White else LightTextGray
            )
        }

        Text(
                modifier = Modifier.align(Alignment.Center),
                text = quality,
                color = if (selected) Color.White else LightTextGray,
                style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                )
        )
    }
}
@Composable
fun GoPremiumButton(onClick : () -> Unit){
    Row(
            modifier = Modifier.clickable{
                onClick()
            },
            verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
               painter = painterResource(Res.drawable.ic_crown),
                contentDescription = null,
                tint =  Color(0xFFD97706),
                modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
                text = "Go Premium",
                style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                )
        )
    }
}

//
// @Preview(
//        name = "Light Theme",
//        showBackground = true,
//        uiMode = Configuration.UI_MODE_NIGHT_NO
// )
//
// @Preview(
//        name = "Dark Theme",
//        showBackground = true,
//        //        uiMode = Configuration.UI_MODE_NIGHT_YES
//        )
// @Composable
// fun Preview() {
//        AppTheme {
//                Surface {
//                        HomeScreen(
//                                viewModel =
//                                        HomeViewModel(FakeHomeRepositoryImpl(), FakeFileManager())
//                        )
//                }
//        }
// }
