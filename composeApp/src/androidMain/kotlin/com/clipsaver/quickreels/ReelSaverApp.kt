package com.clipsaver.quickreels

import android.app.Application
import android.util.Log
import com.clipsaver.ads.InterstitialAdManager
import com.clipsaver.ads.RewardedAdManager
import com.clipsaver.quickreels.data.remote.firebase.FirebaseAppCheckImpl
import com.clipsaver.quickreels.data.remote.firebase.FirebaseConfig
import com.clipsaver.quickreels.data.remote.firebase.FirebaseCrashlyticsImpl
import com.clipsaver.quickreels.di.initKoin
import com.clipsaver.quickreels.purchases.configureRevenueCat
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import org.koin.android.ext.koin.androidContext

class ReelSaverApp : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                if(BuildConfig.DEBUG) DebugAppCheckProviderFactory.getInstance() else PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        Firebase.initialize(context = this)

        configureRevenueCat(BuildConfig.REVENUE_CAT_KEY)

        initKoin(
                appCheck = FirebaseAppCheckImpl(),
                appConfig = FirebaseConfig(),
                crashEvents = FirebaseCrashlyticsImpl()
        ){
            androidContext(this@ReelSaverApp)
        }

        MobileAds.initialize(this) { initializationStatus ->
            Log.d("AdMob", "Initialized: $initializationStatus")
        }
        RewardedAdManager.load(this, Constants.ADS.REWARDED_AD_ID, onDone = {})
//        InterstitialAdManager.load(this,Constants.ADS.INTERSTITIAL_AD_ID, onDone = {})
    }
}
