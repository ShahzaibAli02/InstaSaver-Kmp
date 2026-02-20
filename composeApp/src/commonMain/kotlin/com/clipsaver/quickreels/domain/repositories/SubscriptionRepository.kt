package com.clipsaver.quickreels.domain.repositories
import kotlinx.coroutines.flow.StateFlow

interface SubscriptionRepository
{

    suspend fun loadPurchases()
     fun setPremiumStatus(isPremium: Boolean)
    fun isPremium() : StateFlow<Boolean>
}