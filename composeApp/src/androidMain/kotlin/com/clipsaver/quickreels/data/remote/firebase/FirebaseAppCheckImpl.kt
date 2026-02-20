package com.clipsaver.quickreels.data.remote.firebase

import android.util.Log
import com.clipsaver.quickreels.AppCheck
import com.google.firebase.appcheck.FirebaseAppCheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAppCheckImpl : AppCheck
{

    var token : String = ""
    init
    {
        loadAppCheckToken()
    }
    fun loadAppCheckToken(){
        CoroutineScope(Dispatchers.IO).launch {
            token = getAppCheckToken() ?: ""
        }
    }
    override suspend fun getAppCheckToken(): String?
    {    val appCheck = FirebaseAppCheck.getInstance()
        if(token.isNotBlank()){
            return token
        }
        return runCatching {
            val tokenResult = appCheck.getAppCheckToken(false).await()
            token = tokenResult.token
            token
        }.onFailure {
            Log.e("FirebaseAppCheckImpl() ", "getAppCheckToken ERROR : ${it.message}")
            it.printStackTrace()
        }.getOrNull()
    }
}