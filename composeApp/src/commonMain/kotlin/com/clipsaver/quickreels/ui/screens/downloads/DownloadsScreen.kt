package com.clipsaver.quickreels.ui.screens.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.presentation.viewmodels.DownloadsViewModel
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.ui.components.DownloadVideoItem
import com.clipsaver.quickreels.ui.components.HeaderTitle
import com.clipsaver.quickreels.ui.components.VideoPlayerDialog
import com.clipsaver.quickreels.ui.theme.BackgroundOffWhite
import com.clipsaver.quickreels.ui.theme.LightTextGray
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.LaunchedEffect
import com.clipsaver.quickreels.ads.Ads
import com.clipsaver.quickreels.common.AnalyticsHelper
import org.koin.compose.getKoin

@Composable fun DownloadsScreen(
    viewModel: DownloadsViewModel = koinViewModel(),
    isPremium: Boolean,
    onShowAd: (adType: Ads.TYPE, onDone: () -> Unit) -> Unit,
    onLoadAd: (adType: Ads.TYPE, onDone: (Boolean) -> Unit) -> Unit,
)
{
    val analyticsHelper: AnalyticsHelper = getKoin().get()
    LaunchedEffect(Unit) {
        analyticsHelper.logScreenView(
                Strings.Analytics.Screens.Downloads,
                Strings.Analytics.Screens.Downloads
        )
    }

    fun onVideoClicked(video : Video){


        fun action(){
            viewModel.showVideoPlayer = true
            viewModel.videoPath = video.videoPath
            viewModel.thumbnailPath = video.thumbnailPath
        }
        action()

//        if(isPremium){
//            action()
//            return
//        }
//
//
//        onShowAd(Ads.TYPE.INTERSTITIAL){
//            onLoadAd(Ads.TYPE.INTERSTITIAL,{})
//            action()
//        }
    }

    val videos by viewModel.videos.collectAsState()
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
                onShareClick = { viewModel.shareVideo(viewModel.videoPath) })
    }
    Scaffold(
            topBar = {
                HeaderTitle(modifier = Modifier.fillMaxWidth()
                    .padding(top = 30.dp)
                    .padding(bottom = 24.dp)
                        ,title = Strings.Downloads)

            },
            content = { paddingValues ->

                Column(
                        modifier = Modifier.fillMaxSize().background(BackgroundOffWhite)
                            .padding(paddingValues)
                ) {
                    if (videos.isEmpty())
                    {
                        EmptyMessage()
                    } else
                    {
                        val sortedVideos = videos.sortedWith(compareByDescending<Video> { it.isDownloading || it.isFailed }.thenByDescending { it.timestamp })

                        LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(sortedVideos) { video ->
                                DownloadVideoItem(
                                        video = video,
                                        onDeleteClick = { viewModel.deleteVideo(video) },
                                        onShareClick = { viewModel.shareVideo(video.videoPath) },
                                        onCancelDownload = { viewModel.cancelDownload(video) },
                                        onRetryClick = { viewModel.retryDownload(video) },
                                        onVideoClick = {
                                            onVideoClicked(video)


                                        })
                            }
                        }
                    }
                }

            })


}

@Composable private fun EmptyMessage()
{
    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Download,
                    contentDescription = null,
                    tint = LightTextGray,
                    modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                    text = Strings.NoDownloads, // Or "No Downloads Yet" using existing
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                            color = LightTextGray
                    ),
            )
        }
    }
}
