package com.clipsaver.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

object RewardedInterstitialAdManager {

    private var rewardedAd: RewardedInterstitialAd? = null
    private var isLoading = false
    private var  mOnDone: (Boolean) -> Unit = {}
    fun load(context: Context, adUnitId: String, onDone: (Boolean) -> Unit) {
        this.mOnDone = onDone
        if(isLoading){
            return
        }
        if(rewardedAd !=null){
            mOnDone(true)
            return
        }
        isLoading = true

        RewardedInterstitialAd.load(
            context,
            adUnitId, AdManagerAdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedAd = ad
                    isLoading = false
                    mOnDone(true)
                    Log.d("RewardedInterstitialAdManager()", "Rewarded Ad loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isLoading = false
                    mOnDone(false)
                    Log.e("RewardedInterstitialAdManager()", "Failed to load: $error")
                }
            }
        )
    }

    fun show(activity: Activity, onRewardEarned: () -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(activity) { rewardItem: RewardItem ->
                onRewardEarned()
            }
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                }


                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    rewardedAd = null
                    onRewardEarned()
                    Log.e("RewardedInterstitialAdManager()", "Failed to show ad: $error")
                }
            }
        }
    }

    fun isReady(): Boolean = rewardedAd != null
}
