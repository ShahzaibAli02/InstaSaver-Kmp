package com.clipsaver.quickreels

object Constants
{
    const val STORE_LINK = "https://play.google.com/store/apps/details?id=com.clipsaver.quickreels"


    object ADS
    {

        val REWARDED_AD_ID = if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/5224354917"
        } else
        {
            "RELEASE ID"
        }

        val REWARDED_INTERSTITIAL_AD_ID = if (BuildConfig.DEBUG)
        {
            "ca-app-pub-3940256099942544/6978759866"
        } else
        {
            "RELEASE ID"
        }

        val INTERSTITIAL_AD_ID = if (BuildConfig.DEBUG) {
            "ca-app-pub-3940256099942544/4411468910"
        } else
        {
            "RELEASE ID"
        }
    }

}