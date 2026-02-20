package com.clipsaver.quickreels.purchases


import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure


fun configureRevenueCat(key: String)
{

    Purchases.configure(apiKey = key)
}
