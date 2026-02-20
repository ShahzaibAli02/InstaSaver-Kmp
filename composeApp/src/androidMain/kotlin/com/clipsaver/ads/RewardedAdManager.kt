package com.clipsaver.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object RewardedAdManager {

    private var rewardedAd: RewardedAd? = null
    private var isLoading = false

    private var  mOnDone: (Boolean) -> Unit = {}

    fun load(context: Context, adUnitId: String, onDone: (Boolean) -> Unit) {
        Log.d("RewardedAdManager()", "load()")
        this.mOnDone = onDone
        if(isLoading){
            Log.d("RewardedAdManager()", "isLoading return")
            return
        }
        if(rewardedAd!=null){
            mOnDone(true)
            return
        }
        isLoading = true

        RewardedAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {

                    rewardedAd = ad
                    isLoading = false
                    mOnDone(true)
                    Log.d("RewardedAdManager()", "Rewarded Ad loaded")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {

                    rewardedAd = null
                    isLoading = false
                    mOnDone(false)
                    Log.e("RewardedAdManager()", "Failed to load: $error")
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
                    Log.e("AdMob", "Failed to show ad: $error")
                }
            }
        }
    }

    fun isReady(): Boolean {
        Log.d("RewardedAdManager()", "isReady() = ${rewardedAd != null}")
        return rewardedAd != null
    }
}
