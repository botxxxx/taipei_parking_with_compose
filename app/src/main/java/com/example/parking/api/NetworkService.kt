package com.example.parking.api

import android.util.Log
import com.example.parking.api.data.AVL_001_Rs
import com.example.parking.api.model.BaseCallBack
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.api.data.DESC_001_Rs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object NetworkService {

    private lateinit var apiService: ApiService
    private lateinit var jsonService: JsonService

    fun init() {
        apiService = ApiService.create()
        jsonService = JsonService.create()
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

    fun getParkingDescRequest(callback: BaseCallBack<DESC_001_Rs>) {
        val safeCoroutineScope = callback.baseViewInterface.getLifeCycleScope()
        safeCoroutineScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val request = jsonService.getParkingDesc()
                    Log.e("getParkingDescRequest", "$request")
                    callback.getResponse(request)
                }
            } catch (ex: Exception) {
                Log.e("getParkingDescRequest", "sendAppRequest fail: ${Log.getStackTraceString(ex)}")
                callback.onFailure()
            }
        }
    }

    fun getParkingAvailableRequest(callback: BaseCallBack<AVL_001_Rs>) {
        val safeCoroutineScope = callback.baseViewInterface.getLifeCycleScope()
        safeCoroutineScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val request = jsonService.getParkingAvailable()
                    Log.e("getParkingAvailableRequest", "$request")
                    callback.getResponse(request)
                }
            } catch (ex: Exception) {
                Log.e("getParkingAvailableRequest", "sendAppRequest fail: ${Log.getStackTraceString(ex)}")
                callback.onFailure()
            }
        }
    }
}