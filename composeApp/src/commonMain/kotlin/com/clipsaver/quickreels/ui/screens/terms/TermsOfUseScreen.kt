package com.clipsaver.quickreels.ui.screens.terms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clipsaver.quickreels.ui.webview.WebView
import com.clipsaver.quickreels.utils.Constants
//import com.multiplatform.webview.web.WebView
//import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun TermsOfUseScreen(onTermsAccepted: () -> Unit) {
    var isAccepted by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // WebView takes up most of the space
            WebView(url = Constants.TERMS_OF_USE_URL, modifier = Modifier.weight(1f).fillMaxWidth())

            // Bottom section with acceptance and continue button
            Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable{
                            isAccepted = !isAccepted
                        },
                        horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(checked = isAccepted, onCheckedChange = { isAccepted = it })
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                            text = "I accept the Terms of Use",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                            )
                    )
                }

                Button(
                        onClick = onTermsAccepted,
                        enabled = isAccepted,
                        modifier = Modifier.fillMaxWidth()
                ) { Text("Continue") }
            }
        }
    }
}
