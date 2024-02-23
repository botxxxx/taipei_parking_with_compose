package com.example.parking.api

import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.api.data.UPDATE_001_Rs
import retrofit2.http.*
import java.util.*

interface ApiService {
    // 請參考下圖一頁面設計，實作一個登入頁面，讓使用者輸入下列帳號與密碼登入，並實作簡單 的格式驗證以及錯誤提示。
    // https://noodoe-app-development.web.app/api/login
    @Headers("X-Parse-Application-Id: vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD")
    @POST("api/login")
    @FormUrlEncoded
    suspend fun doLogin(
        @Field("username") username: String,
        @Field("password") password: String,
    ): LOGIN_001_Rs

    @Headers("X-Parse-Application-Id: vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD")
    @PUT("api/users/{objectId}")
    @FormUrlEncoded
    suspend fun doUpdate(
        @Header("X-Parse-Session-Token") sessionToken: String?,
        @Path("objectId") objectId: String?,
        @Field("phone") phone: String?,
        @Field("timezone") timezone: String?,
    ): UPDATE_001_Rs
}