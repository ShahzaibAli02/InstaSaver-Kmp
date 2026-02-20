package com.clipsaver.quickreels.ui.screens.paywall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallListener
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

@Composable fun PaywallDialogScreen(
    onClose: () -> Unit = {},
    onAppear : () -> Unit  = {},
    onPurchaseSuccess: () -> Unit = {},
    onPurchaseFailed: (message: String) -> Unit = {}
)
{

    LaunchedEffect(Unit){
        onAppear()
    }
    val options = remember {
        PaywallOptions(
                dismissRequest = {
                    onClose()
                }
                , builder = {
            listener = object  : PaywallListener {

                override fun onPurchaseCancelled()
                {
                    super.onPurchaseCancelled()
                    onPurchaseFailed("Purchase Cancelled")
                }

                override fun onRestoreError(error: PurchasesError)
                {
                    super.onRestoreError(error)
                    onPurchaseFailed(error.message)
                }

                override fun onRestoreCompleted(customerInfo: CustomerInfo)
                {
                    super.onRestoreCompleted(customerInfo)
                    if(customerInfo.entitlements.active.any()){
                        onPurchaseSuccess()
                    }else{
                        onPurchaseFailed("No Active Subscription")
                    }
                }

                override fun onPurchaseCompleted(
                    customerInfo: CustomerInfo,
                    storeTransaction: StoreTransaction
                )
                {
                    super.onPurchaseCompleted(
                            customerInfo,
                            storeTransaction
                    )
                    onPurchaseSuccess()
                }

                override fun onPurchaseError(error: PurchasesError)
                {
                    super.onPurchaseError(error)
                    onPurchaseFailed(error.message)
                }
            }
        }
        )
    }
    Dialog(onDismissRequest = {},properties = DialogProperties(usePlatformDefaultWidth = false)){
        Paywall(
                options =options
        )
    }

}