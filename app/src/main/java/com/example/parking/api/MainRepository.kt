package com.example.parking.api

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class MainRepository @Inject constructor(
    @Named("Login") private val service: Retrofit
) {
    private val serviceApi = service.create(ApiService::class.java)

    @WorkerThread
    fun sendLoginRequest(
        login: LOGIN_001_Rq,
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit
    ): Flow<LOGIN_001_Rs> = flow {
        try {
            val request = serviceApi.doLogin(login.user, login.pwd)
            Log.e("request", "$request")
            emit(request)
        } catch (ex: Exception) {
            Log.e("error", "sendAppRequest fail: ${Log.getStackTraceString(ex)}")
            onError()
        }
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)
}
