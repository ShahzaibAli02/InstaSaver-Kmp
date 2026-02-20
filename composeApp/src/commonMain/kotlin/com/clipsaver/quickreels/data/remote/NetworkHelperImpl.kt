package com.clipsaver.quickreels.data.remote

import com.clipsaver.quickreels.AppCheck
import com.clipsaver.quickreels.CrashEvents
import com.clipsaver.quickreels.CrashEvents.Type
import com.clipsaver.quickreels.Platform
import com.clipsaver.quickreels.ReelFile
import com.clipsaver.quickreels.data.remote.models.TagsResponse
import com.clipsaver.quickreels.data.remote.models.VideoResponse
import com.clipsaver.quickreels.domain.repositories.LocalRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.prepareGet
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

class NetworkHelperImpl(
    private val localRepository: LocalRepository,
    private  val appCheck: AppCheck,
    private  val crashEvents: CrashEvents,
    private val platform: Platform) : NetworkHelper {

//    val baseURL : String = "http://192.168.100.105:3004"
    val baseURL : String =  "http://192.168.100.105:3004"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
            )
        }
    }

    @Serializable private data class VideoRequest(val url: String)
    @Serializable
    private data class TagsRequest(val content: String)

    override suspend fun fetchVideo(url: String): Result<VideoResponse> {
        var errorMessage = ""
        return try {
            val appCheckToken = appCheck.getAppCheckToken() ?: ""
            if(appCheckToken.isBlank()){
                crashEvents.logIssue("appCheckToken is Unexpectedly Blank", type = Type.ERROR)
            }
            val response: VideoResponse = client.post("${baseURL}/getVideo") {
                                header("Content-Type", "application/json")
                                header("X-Platform", platform.type.toString())
                                header("X-AppCheck-Token", appCheckToken)
                                contentType(ContentType.Application.Json)
                                setBody(VideoRequest(url))
                            }
                            .body()

            if (response.error == true) {
                errorMessage = response.message ?: "Something went wrong. Please check if the video link is correct and try again."
                Result.failure(Exception(response.message ?: "Something went wrong. Please check if the video link is correct and try again."))
            } else {
                Result.success(response)
            }

        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            errorMessage = "No internet connection. Please check your network and try again."
            Result.failure(Exception("No internet connection. Please check your network and try again."))
        } catch (e: ConnectTimeoutException) {
            e.printStackTrace()
            errorMessage = "Connection timed out. Please try again."
            Result.failure(Exception("Connection timed out. Please try again."))
        } catch (e: HttpRequestTimeoutException) {
            e.printStackTrace()
            errorMessage = "Request timed out. The server might be busy."
            Result.failure(Exception("Request timed out. The server might be busy."))
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message ?: "An unexpected error occurred. Please try again."
            Result.failure(Exception("An unexpected error occurred. Please try again later."))
        }
        finally
        {
            if(errorMessage.isNotBlank())
            {
                crashEvents.logIssue("NetworkHelperImpl() $errorMessage", CrashEvents.Type.ERROR)
            }
        }
    }

    override suspend fun fetchHashTags(content: String): Result<TagsResponse>
    {
        var errorMessage = ""
        return try
        {
            val appCheckToken = appCheck.getAppCheckToken() ?: ""
            if (appCheckToken.isBlank())
            {
                crashEvents.logIssue(
                        "appCheckToken is Unexpectedly Blank",
                        type = Type.ERROR
                )
            }
            val response: TagsResponse = client.post("${baseURL}/fetchTags") {
                header(
                        "Content-Type",
                        "application/json"
                )
                header(
                        "X-Platform",
                        platform.type.toString()
                )
                header(
                        "X-AppCheck-Token",
                        appCheckToken
                )
                contentType(ContentType.Application.Json)
                setBody(TagsRequest(content))
            }.body()

            if (response.error == true)
            {
                errorMessage = response.message
                    ?: "Something went wrong. Please check if the video link is correct and try again."
                Result.failure(
                        Exception(
                                response.message
                                    ?: "Something went wrong. Please check if the video link is correct and try again."
                        )
                )
            } else
            {
                Result.success(response)
            }

        } catch (e: SocketTimeoutException)
        {
            e.printStackTrace()
            errorMessage = "No internet connection. Please check your network and try again."
            Result.failure(Exception("No internet connection. Please check your network and try again."))
        } catch (e: ConnectTimeoutException)
        {
            e.printStackTrace()
            errorMessage = "Connection timed out. Please try again."
            Result.failure(Exception("Connection timed out. Please try again."))
        } catch (e: HttpRequestTimeoutException)
        {
            e.printStackTrace()
            errorMessage = "Request timed out. The server might be busy."
            Result.failure(Exception("Request timed out. The server might be busy."))
        } catch (e: Exception)
        {
            e.printStackTrace()
            errorMessage = e.message ?: "An unexpected error occurred. Please try again."
            Result.failure(Exception("An unexpected error occurred. Please try again later."))
        } finally
        {
            if (errorMessage.isNotBlank())
            {
                crashEvents.logIssue(
                        "NetworkHelperImpl() $errorMessage",
                        CrashEvents.Type.ERROR
                )
            }
        }
    }

    override suspend fun downloadFile(url: String, destinationPath: String): Flow<DownloadState> =
            flow {
                var errorMessage = ""
                val file = ReelFile(destinationPath)
                emit(DownloadState.Progress(0f))
                try {
                    client.prepareGet(url).execute { httpResponse ->
                        val channel = httpResponse.bodyAsChannel()
                        val contentLength = httpResponse.contentLength()
                        var totalBytesRead = 0L

                        while (!channel.isClosedForRead) {
                            val buffer = ByteArray(8192)
                            val read = channel.readAvailable(buffer, 0, buffer.size)
                            if (read == -1) break
                            if (read > 0) {
                                val chunk = buffer.copyOf(read)
                                file.write(chunk)
                                //
                                // fileManager.appendToFile(destinationPath, chunk)
                                totalBytesRead += read
                                if (contentLength != null) {
                                    emit(
                                            DownloadState.Progress(
                                                    totalBytesRead.toFloat() / contentLength
                                            )
                                    )
                                }
                            }
                        }
                        emit(DownloadState.Success(destinationPath))
                    }
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                    errorMessage = "No internet connection."
                    emit(DownloadState.Error("No internet connection."))
                } catch (e: ConnectTimeoutException) {
                    e.printStackTrace()
                    errorMessage = "Connection timed out."
                    emit(DownloadState.Error("Connection timed out."))
                } catch (e: HttpRequestTimeoutException) {
                    e.printStackTrace()
                    errorMessage = "Request timed out."
                    emit(DownloadState.Error("Request timed out."))
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorMessage = e.message ?: "An unexpected error occurred."
                    emit(DownloadState.Error("Download failed: An unexpected error occurred."))
                } finally {
                    file.close()
                    if(errorMessage.isNotBlank())
                    {
                        crashEvents.logIssue("NetworkHelperImpl() fetchVideo() $errorMessage", CrashEvents.Type.ERROR)
                    }
                }
            }
}

class FakeNetworkHelperImpl(private val appCheck: AppCheck) : NetworkHelper {
    override suspend fun fetchVideo(url: String): Result<VideoResponse> {
        println("NetworkHelperImpl() INTEGRITY TOKEN ${appCheck.getAppCheckToken()}")
        val random = Random.nextInt(0,100)
        val json =
                """
            {
              "url": "https://www.youtube.com/watch?v=SO8DcUIwV0M",
              "source": "youtube",
              "title": "Sample Video $random",
              "author": "",
              "thumbnail": "https://picsum.photos/400/400",
              "duration": 175,
              "medias": [
                {
                  "formatId": 137,
                  "label": "mp4 (720p)",
                  "type": "video",
                  "ext": "mp4",
                  "quality": "mp4 (720p)",
                  "width": 1080,
                  "height": 1080,
                  "url": "https://redirector.googlevideo.com/videoplayback?expire=1764697959&ei=B9MuaeGkHOfUxN8P8uqoiQQ&ip=176.1.200.46&id=o-AExAam5eljrmqGR6rB9blEgTFDlRmqxSOtazf1gEZBjh&itag=137&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&cps=12&met=1764676359%2C&mh=rR&mm=31%2C29&mn=sn-uxax4vopj5qx-q0n6%2Csn-4g5edndd&ms=au%2Crdu&mv=m&mvi=4&pcm2cms=yes&pl=21&rms=au%2Cau&gcr=de&initcwndbps=1618750&bui=AdEuB5SknIxvIoBIiCjGzWSoLD9J7izHGxDNgRmUuCWG3HerAh7hzVyHfudt4vtDYylwb0hY3Ob51gE2&spc=6b0G_LzEairA&vprv=1&svpuc=1&mime=video%2Fmp4&rqh=1&gir=yes&clen=1299443&dur=174.920&lmt=1759650738943495&mt=1764675901&fvip=2&keepalive=yes&fexp=51552689%2C51565116%2C51565682%2C51580968&c=ANDROID&txp=1432534&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cgcr%2Cbui%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRQIgOGNAIBocdZaWvUnuFH4lHro-9yieVwwlCKUKwtavV2ICIQD8ds7s9giA8VLaiJYdFTHmYikObJJ7cHQgxeCEmWOrqw%3D%3D&lsparams=cps%2Cmet%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpcm2cms%2Cpl%2Crms%2Cinitcwndbps&lsig=APaTxxMwRQIhAOECddMWKB3bat5zK2Qm9AIL4qbknJFSltjm_xUHoocOAiA09RQmt-gPDuO6d_XQl6REOq_2TUfp8IqQj_i-ITOYwA%3D%3D",
                  "bitrate": 62309,
                  "fps": 25,
                  "audioQuality": null,
                  "audioSampleRate": null,
                  "mimeType": "video/mp4; codecs=\"avc1.640020\"",
                  "duration": 2,
                  "extension": "mp4"
                },
                {
                  "formatId": 137,
                  "label": "mp4 (1080p)",
                  "type": "video",
                  "ext": "mp4",
                  "quality": "mp4 (1080p)",
                  "width": 1080,
                  "height": 1080,
                  "url": "https://redirector.googlevideo.com/videoplayback?expire=1764697959&ei=B9MuaeGkHOfUxN8P8uqoiQQ&ip=176.1.200.46&id=o-AExAam5eljrmqGR6rB9blEgTFDlRmqxSOtazf1gEZBjh&itag=137&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&cps=12&met=1764676359%2C&mh=rR&mm=31%2C29&mn=sn-uxax4vopj5qx-q0n6%2Csn-4g5edndd&ms=au%2Crdu&mv=m&mvi=4&pcm2cms=yes&pl=21&rms=au%2Cau&gcr=de&initcwndbps=1618750&bui=AdEuB5SknIxvIoBIiCjGzWSoLD9J7izHGxDNgRmUuCWG3HerAh7hzVyHfudt4vtDYylwb0hY3Ob51gE2&spc=6b0G_LzEairA&vprv=1&svpuc=1&mime=video%2Fmp4&rqh=1&gir=yes&clen=1299443&dur=174.920&lmt=1759650738943495&mt=1764675901&fvip=2&keepalive=yes&fexp=51552689%2C51565116%2C51565682%2C51580968&c=ANDROID&txp=1432534&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cgcr%2Cbui%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRQIgOGNAIBocdZaWvUnuFH4lHro-9yieVwwlCKUKwtavV2ICIQD8ds7s9giA8VLaiJYdFTHmYikObJJ7cHQgxeCEmWOrqw%3D%3D&lsparams=cps%2Cmet%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpcm2cms%2Cpl%2Crms%2Cinitcwndbps&lsig=APaTxxMwRQIhAOECddMWKB3bat5zK2Qm9AIL4qbknJFSltjm_xUHoocOAiA09RQmt-gPDuO6d_XQl6REOq_2TUfp8IqQj_i-ITOYwA%3D%3D",
                  "bitrate": 62309,
                  "fps": 25,
                  "audioQuality": null,
                  "audioSampleRate": null,
                  "mimeType": "video/mp4; codecs=\"avc1.640020\"",
                  "duration": 2,
                  "extension": "mp4"
                },
                {
                  "formatId": 1373,
                  "label": "mp4 (480p)",
                  "type": "video",
                  "ext": "mp4",
                  "quality": "mp4 (480p)",
                  "width": 1080,
                  "height": 1080,
                  "url": "https://redirector.googlevideo.com/videoplayback?expire=1764697959&ei=B9MuaeGkHOfUxN8P8uqoiQQ&ip=176.1.200.46&id=o-AExAam5eljrmqGR6rB9blEgTFDlRmqxSOtazf1gEZBjh&itag=137&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&cps=12&met=1764676359%2C&mh=rR&mm=31%2C29&mn=sn-uxax4vopj5qx-q0n6%2Csn-4g5edndd&ms=au%2Crdu&mv=m&mvi=4&pcm2cms=yes&pl=21&rms=au%2Cau&gcr=de&initcwndbps=1618750&bui=AdEuB5SknIxvIoBIiCjGzWSoLD9J7izHGxDNgRmUuCWG3HerAh7hzVyHfudt4vtDYylwb0hY3Ob51gE2&spc=6b0G_LzEairA&vprv=1&svpuc=1&mime=video%2Fmp4&rqh=1&gir=yes&clen=1299443&dur=174.920&lmt=1759650738943495&mt=1764675901&fvip=2&keepalive=yes&fexp=51552689%2C51565116%2C51565682%2C51580968&c=ANDROID&txp=1432534&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cgcr%2Cbui%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRQIgOGNAIBocdZaWvUnuFH4lHro-9yieVwwlCKUKwtavV2ICIQD8ds7s9giA8VLaiJYdFTHmYikObJJ7cHQgxeCEmWOrqw%3D%3D&lsparams=cps%2Cmet%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpcm2cms%2Cpl%2Crms%2Cinitcwndbps&lsig=APaTxxMwRQIhAOECddMWKB3bat5zK2Qm9AIL4qbknJFSltjm_xUHoocOAiA09RQmt-gPDuO6d_XQl6REOq_2TUfp8IqQj_i-ITOYwA%3D%3D",
                  "bitrate": 62309,
                  "fps": 25,
                  "audioQuality": null,
                  "audioSampleRate": null,
                  "mimeType": "video/mp4; codecs=\"avc1.640020\"",
                  "duration": 2,
                  "extension": "mp4"
                },
                {
                  "formatId": 18,
                  "label": "mp4 (360p)",
                  "type": "video",
                  "ext": "mp4",
                  "quality": "mp4 (360p)",
                  "width": 360,
                  "height": 360,
                  "url": "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                  "bitrate": 143078,
                  "fps": 25,
                  "audioQuality": "AUDIO_QUALITY_LOW",
                  "audioSampleRate": "44100",
                  "mimeType": "video/mp4; codecs=\"avc1.42001E, mp4a.40.2\"",
                  "duration": 2,
                  "is_audio": true,
                  "extension": "mp4"
                },
                {
                  "formatId": 140,
                  "label": "m4a (131kb/s)",
                  "type": "audio",
                  "ext": "m4a",
                  "width": null,
                  "height": null,
                  "url": "https://redirector.googlevideo.com/videoplayback?expire=1764697959&ei=B9MuaeGkHOfUxN8P8uqoiQQ&ip=176.1.200.46&id=o-AExAam5eljrmqGR6rB9blEgTFDlRmqxSOtazf1gEZBjh&itag=140&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&cps=12&met=1764676359%2C&mh=rR&mm=31%2C29&mn=sn-uxax4vopj5qx-q0n6%2Csn-4g5edndd&ms=au%2Crdu&mv=m&mvi=4&pcm2cms=yes&pl=21&rms=au%2Cau&gcr=de&initcwndbps=1618750&bui=AdEuB5SknIxvIoBIiCjGzWSoLD9J7izHGxDNgRmUuCWG3HerAh7hzVyHfudt4vtDYylwb0hY3Ob51gE2&spc=6b0G_LzEairA&vprv=1&svpuc=1&mime=audio%2Fmp4&rqh=1&gir=yes&clen=2833720&dur=174.923&lmt=1759650591162128&mt=1764675901&fvip=2&keepalive=yes&fexp=51552689%2C51565116%2C51565682%2C51580968&c=ANDROID&txp=1432534&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cgcr%2Cbui%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRQIgZ_d5qjNTwODcCA9KOgqXTvWV3fwj3eQGq93s0FTyXngCIQDIi49cQy4T2HCZK4o7d5yr7V0QKMpWvTznvZAoD2ryTg%3D%3D&lsparams=cps%2Cmet%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpcm2cms%2Cpl%2Crms%2Cinitcwndbps&lsig=APaTxxMwRQIhAOECddMWKB3bat5zK2Qm9AIL4qbknJFSltjm_xUHoocOAiA09RQmt-gPDuO6d_XQl6REOq_2TUfp8IqQj_i-ITOYwA%3D%3D",
                  "bitrate": 131104,
                  "fps": null,
                  "audioQuality": "AUDIO_QUALITY_MEDIUM",
                  "audioSampleRate": "44100",
                  "mimeType": "audio/mp4; codecs=\"mp4a.40.2\"",
                  "duration": 2,
                  "quality": "m4a (131kb/s)",
                  "extension": "m4a"
                },
                {
                  "formatId": 251,
                  "label": "opus (135kb/s)",
                  "type": "audio",
                  "ext": "opus",
                  "width": null,
                  "height": null,
                  "url": "https://redirector.googlevideo.com/videoplayback?expire=1764697959&ei=B9MuaeGkHOfUxN8P8uqoiQQ&ip=176.1.200.46&id=o-AExAam5eljrmqGR6rB9blEgTFDlRmqxSOtazf1gEZBjh&itag=251&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&cps=12&met=1764676359%2C&mh=rR&mm=31%2C29&mn=sn-uxax4vopj5qx-q0n6%2Csn-4g5edndd&ms=au%2Crdu&mv=m&mvi=4&pcm2cms=yes&pl=21&rms=au%2Cau&gcr=de&initcwndbps=1618750&bui=AdEuB5SknIxvIoBIiCjGzWSoLD9J7izHGxDNgRmUuCWG3HerAh7hzVyHfudt4vtDYylwb0hY3Ob51gE2&spc=6b0G_LzEairA&vprv=1&svpuc=1&mime=audio%2Fwebm&rqh=1&gir=yes&clen=2820823&dur=174.941&lmt=1759650593159403&mt=1764675901&fvip=2&keepalive=yes&fexp=51552689%2C51565116%2C51565682%2C51580968&c=ANDROID&txp=1432534&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cgcr%2Cbui%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRgIhAMjlU5Ee4l206g4IC1mEpoGP2empUHbnbKh8HNHajql3AiEAv4L7bAXK0Jhih9vc180oLkp2PC3qT5hQ016K-B3neK8%3D&lsparams=cps%2Cmet%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpcm2cms%2Cpl%2Crms%2Cinitcwndbps&lsig=APaTxxMwRQIhAOECddMWKB3bat5zK2Qm9AIL4qbknJFSltjm_xUHoocOAiA09RQmt-gPDuO6d_XQl6REOq_2TUfp8IqQj_i-ITOYwA%3D%3D",
                  "bitrate": 134949,
                  "fps": null,
                  "audioQuality": "AUDIO_QUALITY_MEDIUM",
                  "audioSampleRate": "48000",
                  "mimeType": "audio/webm; codecs=\"opus\"",
                  "duration": 2,
                  "quality": "opus (135kb/s)",
                  "extension": "opus"
                }
              ],
              "type": "multiple",
              "error": false,
              "time_end": 434
            }
        """.trimIndent()

        val jsonParser = Json { ignoreUnknownKeys = true }
        val response = jsonParser.decodeFromString<VideoResponse>(json)
        return Result.success(response)
    }

    override suspend fun fetchHashTags(content: String): Result<TagsResponse>
    {
        return Result.success(TagsResponse())
    }


    override suspend fun downloadFile(url: String, destinationPath: String): Flow<DownloadState> =
            flow {
                emit(DownloadState.Progress(0.5f))
                kotlinx.coroutines.delay(1000)
                emit(DownloadState.Success(destinationPath))
            }
}
