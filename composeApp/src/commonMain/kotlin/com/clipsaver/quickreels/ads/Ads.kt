package com.clipsaver.quickreels.ads

interface Ads
{
    enum class TYPE {
        REWARDED,
        REWARDED_INTERSTITIAL,
        INTERSTITIAL,
        NATIVE,
        BANNER
    }
    fun isAdReady(type : TYPE): Boolean
    fun loadAd(type: TYPE,onDone: (Boolean) -> Unit)
    fun showAd(type: TYPE,onDone : () -> Unit)
}