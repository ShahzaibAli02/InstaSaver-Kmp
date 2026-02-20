package com.clipsaver.quickreels.ui

object Strings {
    const val share = "Share"
    const val delete = "Delete"
    const val AppName = "InstSaver Pro"
    const val Home = "Home"
    const val NoDownloads = "Nothing to show. Your Collections will appear here."
    const val PasteLinkPlaceholder = "Paste  link..."
    const val Clear = "Clear"
    const val PasteButton = "Paste"
    const val FetchVideoButton = "Go"
    const val RecentDownloadsHeader = "Recent Collections"
    const val LinkIconDescription = "Link"
    const val NoRecentVideos = "No recent videos found"
    const val Settings = "Settings"
    const val Downloads = "Collections"
    const val Schedule = "Scheduled Posts"
    const val HashTagGenertor = "Hashtag Generator"

    const val HowToUse = "How to use this app ?"
    const val HowToUseTitle = "How to use this app"
    const val HowToUseStep1 = "1. Open IG"
    const val HowToUseStep2 = "2. Select your video you wants to Re-Post"
    const val HowToUseStep3 = "3. Click on share then copy link"
    const val HowToUseStep4 = "4. Paste that link here and click on Go"
    const val HowToUseStep5 = "5. Enjoy"

    const val Share_Video_Message = "Check out this video I saved using ${AppName}! Download the app here "


    object Tabs{
        const val Home = "Home"
        const val Downloads = "Collections"
        const val Settings = "Settings"
        const val HashTags = "HashTags"
    }

    object Analytics {
        const val ScreenView = "screen_view"
        const val Click = "click"

        object Screens {
            const val Splash = "Splash"
            const val Home = "Home"
            const val Downloads = "Downloads"
            const val Main = "Main"
            const val Settings = "Settings"
            const val HashtagGenerator = "HashtagGenerator"
        }

        object Events {
            const val PasteLink = "click_paste_link"
            const val ClearLink = "click_clear_link"
            const val FetchVideo = "click_fetch_video"
            const val ViewAllRecent = "click_view_all_recent"
            const val DownloadVideo = "click_download_video"
            const val ShareVideo = "click_share_video"
            const val AD_SHOWN = "ad_shown"
            const val AD_LOADED = "ad_loaded"
            const val AD_FAILED_SHOWN = "ad_failed_shown"
        }

        object Params {

            const val Ad = "ad"
            const val Url = "url"
            const val VideoPath = "video_path"
            const val ScreenName = "screen_name"
            const val ScreenClass = "screen_class"
        }
    }
}
