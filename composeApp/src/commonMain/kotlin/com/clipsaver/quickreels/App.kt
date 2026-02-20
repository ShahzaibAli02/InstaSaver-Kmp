package com.clipsaver.quickreels

import androidx.compose.runtime.*
import com.clipsaver.quickreels.domain.repositories.SubscriptionRepository
import com.clipsaver.quickreels.presentation.viewmodels.MainViewModel
import com.clipsaver.quickreels.ui.screens.main.MainScreen
import com.clipsaver.quickreels.ui.screens.splash.SplashScreen
import com.clipsaver.quickreels.ui.screens.terms.TermsOfUseScreen
import com.clipsaver.quickreels.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    AppTheme {
        val viewModel = koinViewModel<MainViewModel>()
        val  subscriptionRepository  : SubscriptionRepository = koinInject()
        val isTermsAccepted by viewModel.isTermsAccepted.collectAsState()
        var showSplash by remember { mutableStateOf(true) }
        LaunchedEffect(Unit){
            subscriptionRepository.loadPurchases()
        }
        if (showSplash || isTermsAccepted == null) {
            SplashScreen(onNavigateToMain = { showSplash = false })
        }
//        else if (isTermsAccepted == false) {
//            TermsOfUseScreen(onTermsAccepted = { viewModel.acceptTerms() })
//        }

        else {
            MainScreen()
        }
    }
}
