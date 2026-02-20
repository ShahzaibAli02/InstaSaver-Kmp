package com.clipsaver.quickreels.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clipsaver.quickreels.domain.repositories.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: LocalRepository) : ViewModel() {

    private val _showRatingDialog = MutableStateFlow(false)
    val showRatingDialog: StateFlow<Boolean> = _showRatingDialog.asStateFlow()

    private val _isTermsAccepted = MutableStateFlow<Boolean?>(null)
    val isTermsAccepted: StateFlow<Boolean?> = _isTermsAccepted.asStateFlow()

    init {
        checkTermsCondition()
        checkRatingCondition()
    }

    private fun checkTermsCondition() {
        viewModelScope.launch { _isTermsAccepted.value = repository.isTermsAccepted() }
    }

    fun acceptTerms() {
        viewModelScope.launch {
            repository.setTermsAccepted()
            _isTermsAccepted.value = true
        }
    }

    var openCount : Long = 0
    private fun checkRatingCondition() {
        viewModelScope.launch {
            repository.incrementOpenCount()
            openCount = repository.getOpenCount()
            val isRated = repository.isRated()

            if (!isRated && openCount > 0 && openCount % 5 == 0L) {
                _showRatingDialog.value = true
            }
        }
    }

    fun shouldShowPayWall(isPremium : Boolean) : Boolean{
        return if(isPremium) false
        else openCount == 1L || openCount % 6 == 0L
    }

    fun onRateClicked() {
        viewModelScope.launch {
            repository.setRated()
            _showRatingDialog.value = false
        }
    }

    fun onDismissRating() {
        _showRatingDialog.value = false
    }
}
