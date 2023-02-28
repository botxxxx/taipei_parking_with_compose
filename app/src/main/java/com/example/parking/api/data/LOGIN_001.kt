package com.example.parking.api.data

import com.example.parking.api.model.BaseModel
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class LOGIN_001_Rq(
    @Field("username") val user: String = "hw001@noodoe.com",
    @Field("password") val pwd: String = "homework",
) : BaseModel()

data class LOGIN_001_Rs(
    @field:SerializedName("objectId") val objectId: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("timezone") val timezone: String?,
    @field:SerializedName("phone") val phone: String?,
    @field:SerializedName("createdAt") val createdAt: String?,
    @field:SerializedName("updatedAt") val updatedAt: String?,
    @field:SerializedName("sessionToken") val sessionToken: String?,
) : BaseModel()