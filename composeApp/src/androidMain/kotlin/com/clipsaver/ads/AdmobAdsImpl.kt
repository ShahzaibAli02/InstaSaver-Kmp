package com.clipsaver.ads

import android.content.Context
import com.clipsaver.quickreels.Constants
import com.clipsaver.quickreels.MainActivity
import com.clipsaver.quickreels.ads.Ads
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd

class AdmobAdsImpl(val context: Context): Ads
{
    override fun isAdReady(type: Ads.TYPE): Boolean
    {
        if(type == Ads.TYPE.REWARDED)
        {
            return RewardedAdManager.isReady()
        }

        if(type == Ads.TYPE.INTERSTITIAL)
        {
            return InterstitialAdManager.isReady()
        }

        if(type == Ads.TYPE.REWARDED_INTERSTITIAL)
        {
            return RewardedInterstitialAdManager.isReady()
        }

        return false
    }

    override fun loadAd(type: Ads.TYPE, onDone: (Boolean) -> Unit)
    {

        if(type == Ads.TYPE.REWARDED)
        {
            RewardedAdManager.load(
                    context,
                    Constants.ADS.REWARDED_AD_ID,
                    onDone = onDone
            )
            return
        }

        if(type == Ads.TYPE.REWARDED_INTERSTITIAL)
        {
            RewardedInterstitialAdManager.load(
                    context,
                    Constants.ADS.REWARDED_INTERSTITIAL_AD_ID,
                    onDone = onDone
            )
            return
        }

        if(type == Ads.TYPE.INTERSTITIAL)
        {
            InterstitialAdManager.load(
                    context,
                    Constants.ADS.INTERSTITIAL_AD_ID,
                    onDone = onDone
            )
            return
        }
        onDone(false)
    }


    override fun showAd(type: Ads.TYPE, onDone: () -> Unit)
    {

        if(MainActivity.activity == null){
            onDone()
            return
        }
       if(type == Ads.TYPE.REWARDED)
       {
           RewardedAdManager.show(MainActivity.activity!!,onDone)
           return
       }

        if(type == Ads.TYPE.INTERSTITIAL)
       {
           InterstitialAdManager.show(MainActivity.activity!!,onDone)
           return
       }
        if(type == Ads.TYPE.REWARDED_INTERSTITIAL)
        {
            RewardedInterstitialAdManager.show(MainActivity.activity!!,onDone)
            return
        }

        onDone()
    }
}