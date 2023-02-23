package com.example.parking.api

import com.example.parking.api.model.TCMSV_001_Rs
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

interface JsonService {
    //    ● 台北市政府開放資料 - 臺北市停車場資訊V2
    //    ○ https://tcgbusfs.blob.core.windows.net/blobtcmsv/TCMSV_alldesc.json
    //    ● 台北市政府開放資料 - 剩餘停車位數V2
    //    ○ https://tcgbusfs.blob.core.windows.net/blobtcmsv/TCMSV_allavailable.json
    @GET("blobtcmsv/TCMSV_alldesc.json")
    suspend fun getCharge(): TCMSV_001_Rs

    companion object {
        private const val BASE_URL = "https://tcgbusfs.blob.core.windows.net/"
        fun create(): JsonService {
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
                .create(JsonService::class.java)
        }
    }
}