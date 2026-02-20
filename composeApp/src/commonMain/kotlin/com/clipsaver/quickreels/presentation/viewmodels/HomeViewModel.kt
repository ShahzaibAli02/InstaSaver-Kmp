package com.clipsaver.quickreels.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clipsaver.quickreels.data.remote.DownloadState
import com.clipsaver.quickreels.data.remote.models.VideoResponse
import com.clipsaver.quickreels.data.shared.EventType
import com.clipsaver.quickreels.domain.models.Video
import com.clipsaver.quickreels.domain.repositories.HomeRepository
import com.clipsaver.quickreels.ui.Strings
import com.clipsaver.quickreels.utils.Util
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

import com.clipsaver.quickreels.utils.DeepLinkHelper

class HomeViewModel(
    private val repository: HomeRepository,
    private val deepLinkHelper: DeepLinkHelper
) : ViewModel() {

    val sharedUrl = deepLinkHelper.sharedUrl

    fun clearSharedUrl() {
        deepLinkHelper.clearSharedUrl()
    }
    //    val event = repository.events.asSharedFlow()
    private val _recentVideos = MutableStateFlow<List<Video>>(emptyList())
    val recentVideos: StateFlow<List<Video>> = _recentVideos.asStateFlow()

    var showVideoPlayer by mutableStateOf(false)
    var videoPath by mutableStateOf("")
    var thumbnailPath by mutableStateOf("")

//    private val _isLoading = MutableStateFlow(false)
    var isLoading  by mutableStateOf(false)

    private val _videoResponse = MutableStateFlow<VideoResponse?>(null)
    val videoResponse: StateFlow<VideoResponse?> = _videoResponse.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    //    private val _credits = MutableStateFlow(Constants.DEFAULT_TOTAL_CREDITS)
    //    val credits: StateFlow<Int> = _credits.asStateFlow()
    //
    //    private val _totalCredits = MutableStateFlow(Constants.DEFAULT_TOTAL_CREDITS)
    //    val totalCredits: StateFlow<Int> = _totalCredits.asStateFlow()

    private val _showPremiumPayWall = MutableStateFlow(false)
    val showPremiumPaywall: StateFlow<Boolean> = _showPremiumPayWall.asStateFlow()

    //    private val _downloadProgress = MutableStateFlow<Float?>(null)
    //    val downloadProgress: StateFlow<Float?> = _downloadProgress.asStateFlow()

    var isFetchedRecentVideos: Boolean = false
        private set

    private var downloadJobs: HashMap<String, Job> = hashMapOf()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _processingProgress = MutableStateFlow(0f)
    val processingProgress: StateFlow<Float> = _processingProgress.asStateFlow()

    private var processJob: Job? = null

    init {
        receiveEvents()
        refreshCredits()
        //        checkDownloadStatus()
    }

    private fun refreshCredits() {
        viewModelScope.launch {
            //            _credits.value = repository.getCredits()
            //            _totalCredits.value = repository.getTotalCredits()
        }
    }

    fun showPremiumPayWall() {
        _showPremiumPayWall.value = true
    }

    fun dismissPremiumPayWall() {
        _showPremiumPayWall.value = false
    }
    suspend fun checkDownloadStatus() {
        delay(500L)
        recentVideos.value.forEach { item ->
            if (item.isDownloading && !downloadJobs.contains(item.id)) {
                repository.updateVideo(item.copy(isDownloading = false, isFailed = true))
            } else item
        }
    }

    fun receiveEvents() {
        viewModelScope.launch {
            repository.events.collect {
                if (it is EventType.CancelDownload) {
                    cancelDownload(it.video)
                } else if (it is EventType.RetryDownload) {
                    retryDownload(it.video)
                } else if (it is EventType.DeleteVideo) {
                    deleteVideo(it.video)
                } else if (it is EventType.ShareVideo) {
                    shareVideo(it.videoPath)
                }
            }
        }
    }

    fun retryDownload(video: Video) {
        fetchVideo(video.link)
        //        deleteVideo(video)
        //        downloadVideo(
        //                video.name,
        //                video.thumbnailPath,
        //                parseDuration(video.duration),
        //                video.link,
        //                "mp4"
        //        )
    }

    private fun parseDuration(durationString: String): Long {
        val parts = durationString.split(":")
        if (parts.size == 2) {
            val minutes = parts[0].toLongOrNull() ?: 0L
            val seconds = parts[1].toLongOrNull() ?: 0L
            return minutes * 60 + seconds
        }
        return 0L
    }

    fun shareVideo(videoPath: String) {
        repository.shareFile(
                message = Strings.Share_Video_Message + repository.storeLink,
                filePath = videoPath
        )
    }
    fun fetchRecentVideos() {
        isFetchedRecentVideos = true
        viewModelScope.launch {
            repository.getRecentVideos().collect { videos -> _recentVideos.value = videos }
        }
    }

    fun fetchVideo(url: String) {
        repository.closeKeyBoard()
        viewModelScope.launch {
            isLoading = true
            _error.value = null
            _videoResponse.value = null

            val result = repository.fetchVideo(url)

            result.onSuccess { response -> _videoResponse.value = response }.onFailure { exception
                ->
                _error.value = exception.message ?: "Unknown error occurred"
            }

            isLoading = false
        }
    }

    fun sendEvent(event: EventType) {
        viewModelScope.launch { repository.sendEvent(event) }
    }
    fun cancelDownload(video: Video) =
            viewModelScope.launch {
                downloadJobs[video.id]?.cancel()
                downloadJobs.remove(video.id)
                repository.updateVideo(
                        video.copy(isDownloading = false, isFailed = true, progress = 0f)
                )
                //        deleteVideo(video)
            }

    fun deleteVideo(video: Video) {
        viewModelScope.launch {
            repository.deleteVideo(video.id)
            // Also delete the file if it exists
            if (video.videoPath.isNotEmpty()) {
                repository.deleteFile(video.videoPath) // Assuming FileManager has delete
                repository.exposeToGallery(video.videoPath)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun downloadVideo(
            title: String,
            thumbnail: String,
            duration: Double,
            url: String,
            quality: String,
            actualLink: String,
            extension: String,
            isPremium: Boolean
    ) =
            viewModelScope.launch {
                if (!isPremium) {
                    val currentCredits = repository.getCredits()
                    if (currentCredits <= 0) {
                        showPremiumPayWall()
                        return@launch
                    }
                    repository.setCredits(currentCredits - 1)
                    //                    _credits.value = currentCredits - 1
                }

                val videoAlreadyExist =
                        recentVideos
                                .value
                                .firstOrNull {
                                    (it.link.isNotBlank() &&
                                            actualLink.isNotBlank() &&
                                            it.link.trim() == actualLink.trim() &&
                                            it.isFailed)
                                }
                                ?.copy(isFailed = false, isDownloading = true)

                val videoID = "temp_${Clock.System.now().toEpochMilliseconds()}"
                val newVideo =
                        videoAlreadyExist
                                ?: Video(
                                        id = videoID,
                                        name = title,
                                        videoPath = "",
                                        thumbnailPath = thumbnail,
                                        duration = Util.formatDuration(duration),
                                        timestamp = Clock.System.now().toEpochMilliseconds(),
                                        isDownloading = true,
                                        progress = 0f,
                                        quality = quality,
                                        link = actualLink,
                                        isFailed = false
                                )

                downloadJobs[newVideo.id]?.cancel()
                downloadJobs[newVideo.id] = launch {
                    repository.addVideo(newVideo)

                    val fileName = "ReelSaver_${Random.nextInt()}"
                    val filePath = repository.createFile(fileName, extension)

                    if (filePath != null) {
                        val downloader = com.clipsaver.quickreels.BackgroundDownloader()
                        var lastProgress = 0f
                        downloader.downloadFile(url, filePath).collect { state ->
                            when (state) {
                                is DownloadState.Progress -> {
                                    if (state.progress - lastProgress >= 0.05f) {
                                        lastProgress = state.progress
                                        repository.updateVideo(
                                                newVideo.copy(progress = state.progress)
                                        )
                                    }
                                }
                                is DownloadState.Success -> {
                                    println("downloadVideo() DownloadState.Success ")
                                    repository.updateVideo(
                                            newVideo.copy(
                                                    isDownloading = false,
                                                    isDownloaded = true,
                                                    videoPath = state.filePath,
                                                    progress = 1f,
                                                    isFailed = false,
                                                    timestamp =
                                                            Clock.System.now().toEpochMilliseconds()
                                            )
                                    )

                                    if (repository.isSaveToPhotosEnabled() || true) {
                                        repository.exposeToGallery(state.filePath)
                                    }
                                    println("File saved at: ${state.filePath}")
                                }
                                is DownloadState.Error -> {
                                    println("downloadVideo() DownloadState.Error ")
                                    _error.value = state.message
                                    repository.updateVideo(
                                            newVideo.copy(
                                                    isDownloading = false,
                                                    progress = 0f,
                                                    isFailed = true,
                                                    timestamp =
                                                            Clock.System.now().toEpochMilliseconds()
                                            )
                                    )
                                }
                                DownloadState.Idle -> {}
                            }
                        }
                    } else {
                        _error.value = "Failed to create file"
                        repository.updateVideo(
                                newVideo.copy(isDownloading = false, progress = 0f, isFailed = true)
                        )
                    }

                    downloadJobs.remove(newVideo.id)
                }
            }

    fun clearError() {
        _error.value = null
    }

    fun clearVideoResponse() {
        _videoResponse.value = null
    }

    fun cancelProcessing() {
        processJob?.cancel()
        _isProcessing.value = false
        _processingProgress.value = 0f
    }

    @OptIn(ExperimentalTime::class)
    fun processVideo(url: String) {
        repository.closeKeyBoard()
        processJob?.cancel()
        processJob =
                viewModelScope.launch {
                    _isProcessing.value = true
                    _processingProgress.value = 0f
                    _error.value = null

                    // 1. Fetch Video Info
                    var localVideoResponse: VideoResponse? = null
                    val fetchResult = repository.fetchVideo(url)
                    fetchResult.onSuccess { response -> localVideoResponse = response }.onFailure {
                            exception ->
                        _error.value = exception.message ?: "Failed to fetch video info"
                        _isProcessing.value = false
                        return@launch
                    }

                    val response = localVideoResponse ?: return@launch
                    val media = response.getMedia().firstOrNull()
                    if (media == null) {
                        _error.value = "No media found"
                        _isProcessing.value = false
                        return@launch
                    }

                    // 2. Prepare for Download
                    val fileName = "ReelSaver_${Random.nextInt()}"
                    val extension = media.extension ?: "mp4" // Fallback
                    val filePath = repository.createFile(fileName, extension)

                    if (filePath == null) {
                        _error.value = "Failed to create file"
                        _isProcessing.value = false
                        return@launch
                    }

                    // 3. Download
                    val downloader = com.clipsaver.quickreels.BackgroundDownloader()
                    var lastProgress = 0f

                    downloader.downloadFile(media.url ?: "", filePath).collect { state ->
                        when (state) {
                            is DownloadState.Progress -> {
                                if (state.progress - lastProgress >= 0.05f) {
                                    lastProgress = state.progress
                                    _processingProgress.value = state.progress
                                }
                            }
                            is DownloadState.Success -> {
                                // 4. Save to DB
                                val videoID = "video_${Clock.System.now().toEpochMilliseconds()}"
                                val newVideo =
                                        Video(
                                                id = videoID,
                                                name = response.title ?: "Video",
                                                videoPath = state.filePath,
                                                thumbnailPath = response.thumbnail ?: "",
                                                duration =
                                                        Util.formatDuration(
                                                                response.duration ?: 0.0
                                                        ),
                                                timestamp =
                                                        Clock.System.now().toEpochMilliseconds(),
                                                isDownloading = false,
                                                progress = 1f,
                                                quality = media.quality ?: "HD",
                                                link = response.url ?: "",
                                                isFailed = false,
                                                isDownloaded = true
                                        )
                                repository.addVideo(newVideo)
                                if (repository.isSaveToPhotosEnabled()) {
                                    repository.exposeToGallery(state.filePath)
                                }

                                // 5. Show Player
                                videoPath = state.filePath
                                thumbnailPath = response.thumbnail ?: ""
                                showVideoPlayer = true

                                _isProcessing.value = false
                                fetchRecentVideos()
                            }
                            is DownloadState.Error -> {
                                _error.value = state.message
                                _isProcessing.value = false
                            }
                            DownloadState.Idle -> {}
                        }
                    }
                }
    }
}
