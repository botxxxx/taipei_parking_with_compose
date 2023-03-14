package com.example.parking.api

import android.util.Log
import com.example.parking.api.data.*
import com.example.parking.api.model.BaseCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class EntryRepository @Inject constructor(
    @Named("Login") private val service: Retrofit,
    @Named("Parking") private val json: Retrofit
) {
    private val serviceApi = service.create(ApiService::class.java)
    private val jsonUrlApi = json.create(JsonService::class.java)

    fun sendUserUpdate(update: UPDATE_001_Rq, callback: BaseCallBack<UPDATE_001_Rs>) {
        callback.lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val request = serviceApi.doUpdate(update.sessionToken, update.objectId, update.phone, update.timezone)
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
        callback.lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val request = jsonUrlApi.getParkingDesc()
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
        callback.lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val request = jsonUrlApi.getParkingAvailable()
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
