package com.example.parking.api

import com.example.parking.api.model.LOGIN_001_Rs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

interface ApiService {
    // 請參考下圖一頁面設計，實作一個登入頁面，讓使用者輸入下列帳號與密碼登入，並實作簡單 的格式驗證以及錯誤提示。
    // https://noodoe-app-development.web.app/api/login
    @Headers("X-Parse-Application-Id: vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD")
    @POST("api/login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("username") username: String = "hw001@noodoe.com",
        @Field("password") password: String = "homework",
    ): LOGIN_001_Rs

    companion object {
        private const val BASE_URL = "https://noodoe-app-development.web.app/"
        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .retryOnConnectionFailure(true)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}