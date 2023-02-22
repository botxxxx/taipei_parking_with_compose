package com.example.parking.api

import android.util.Log
import com.example.parking.api.model.BaseCallBack
import com.example.parking.api.model.LOGIN_001_Rq
import com.example.parking.api.model.LOGIN_001_Rs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object NetworkService {

    private lateinit var apiService: ApiService

    fun init() {
        apiService = ApiService.create()
    }

    fun sendLoginRequest(login: LOGIN_001_Rq, callback: BaseCallBack<LOGIN_001_Rs>) {
        val safeCoroutineScope = callback.baseViewInterface.getLifeCycleScope()
        safeCoroutineScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val request = apiService.doLogin(login.user, login.pwd)
                    Log.e("request", "$request")
                    callback.getResponse(request)
                }
            } catch (ex: Exception) {
                Log.e("error", "sendAppRequest fail: ${Log.getStackTraceString(ex)}")
                callback.onFailure()
            }
        }
    }
}