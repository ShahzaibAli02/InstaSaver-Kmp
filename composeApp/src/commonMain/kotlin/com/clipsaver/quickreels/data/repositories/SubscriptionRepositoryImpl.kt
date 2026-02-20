package com.clipsaver.quickreels.data.repositories

import com.clipsaver.quickreels.data.local.LocalSharedPref
import com.clipsaver.quickreels.domain.repositories.SubscriptionRepository
import com.revenuecat.purchases.kmp.Purchases
import kotlinx.coroutines.flow.StateFlow

class SubscriptionRepositoryImpl (private val localSharedPref: LocalSharedPref): SubscriptionRepository
{
    val key = "is_premium"
    private val _isPremium = kotlinx.coroutines.flow.MutableStateFlow(localSharedPref.getBool(key,false))
    override suspend fun loadPurchases()
    {

        Purchases.sharedInstance.getCustomerInfo(
                onError = { error -> println("Error: ${error.message}") },
                onSuccess = { customerInfo ->
                    val hasProAccess = customerInfo.entitlements.active["pro"] != null
                    setPremiumStatus(hasProAccess)
//                    _isPremium.value = hasProAccess
                    if (hasProAccess)
                    {
                        println("User has pro access!")
                    } else
                    {
                        println("User does not have pro access")
                    }
                },
        )
    }

    override  fun setPremiumStatus(isPremium: Boolean)
    {
        _isPremium.value = isPremium
        localSharedPref.saveBool(key,isPremium)
    }

    override  fun isPremium(): StateFlow<Boolean>
    {
        return _isPremium
    }


}