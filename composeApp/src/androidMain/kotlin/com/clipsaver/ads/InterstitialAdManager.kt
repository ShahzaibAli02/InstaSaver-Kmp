package com.clipsaver.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

object InterstitialAdManager {

    private var rewardedAd: InterstitialAd? = null
    private var isLoading = false

    fun load(context: Context, adUnitId: String, onDone: (Boolean) -> Unit) {
        println("InterstitialAdManager() load()")
        if (isLoading || rewardedAd != null) {
            onDone(false)
            return
        }
        isLoading = true

        InterstitialAd.load(
            context,
            adUnitId,
                AdManagerAdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    rewardedAd = ad
                    isLoading = false
                    onDone(true)
                    Log.d("InterstitialAdManager()", "Ad loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isLoading = false
                    onDone(false)
                    Log.e("InterstitialAdManager()", "Failed to load: $error")
                }
            }
        )
    }

    fun show(activity: Activity, onRewardEarned: () -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(activity)
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    onRewardEarned()
                    Log.d("InterstitialAdManager()", "onAdDismissedFullScreenContent()")
                }

                override fun onAdClicked()
                {
                    super.onAdClicked()
                    onRewardEarned()
                    Log.d("InterstitialAdManager()", "onAdClicked()")
                }
                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    rewardedAd = null
                    onRewardEarned()
                    Log.e("InterstitialAdManager()", "Failed to show ad: $error")
                }
            }
        }
    }

    fun isReady(): Boolean {
        Log.d("InterstitialAdManager()", "isReady() = ${rewardedAd != null}")
        return  rewardedAd != null
    }
}
