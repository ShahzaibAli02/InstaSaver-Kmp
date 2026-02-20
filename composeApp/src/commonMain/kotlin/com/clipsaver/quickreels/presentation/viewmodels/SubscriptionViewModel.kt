package com.clipsaver.quickreels.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.clipsaver.quickreels.domain.repositories.SubscriptionRepository


class SubscriptionViewModel(private val subscriptionRepository: SubscriptionRepository): ViewModel() {
    var showPayWall by mutableStateOf(false)
        private  set


    suspend fun loadPurchases(){
        subscriptionRepository.loadPurchases()
    }


    fun hidePayWall(){
        showPayWall = false
    }
    fun showPayWall(){
        showPayWall = true
    }

    fun setPremiumStatus(isPremium: Boolean) {
        subscriptionRepository.setPremiumStatus(isPremium)
    }

    fun isPremium() =  subscriptionRepository.isPremium()

}
