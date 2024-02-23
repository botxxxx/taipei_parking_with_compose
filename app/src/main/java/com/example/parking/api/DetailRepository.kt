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

class DetailRepository @Inject constructor(
    @Named("Login") private val service: Retrofit
) {
    private val serviceApi = service.create(ApiService::class.java)

    fun sendUserUpdate(update: UPDATE_001_Rq, callback: BaseCallBack<UPDATE_001_Rs>) {
        try {
            callback.lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    val request = serviceApi.doUpdate(
                        update.sessionToken,
                        update.objectId,
                        update.phone,
                        update.timezone
                    )
                    Log.e("request", "$request")
                    callback.getResponse(request)
                }
            }
        } catch (ex: Exception) {
            Log.e("error", "sendAppRequest fail: ${Log.getStackTraceString(ex)}")
            callback.onFailure()
        }
    }
}
