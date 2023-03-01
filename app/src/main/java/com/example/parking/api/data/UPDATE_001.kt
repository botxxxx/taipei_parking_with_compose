package com.example.parking.api.data

import com.example.parking.api.model.BaseModel
import com.google.gson.annotations.SerializedName

data class UPDATE_001_Rq(
    val sessionToken: String?,
    val objectId: String?,
    val phone: String?,
    val timezone: String?,
) : BaseModel()

data class UPDATE_001_Rs(
    @field:SerializedName("results") val results: List<UPDATE_002_Rs>?,
) : BaseModel()

data class UPDATE_002_Rs(
    @field:SerializedName("objectId") val objectId: String?,
    @field:SerializedName("username") val name: String?,
    @field:SerializedName("phone") val phone: String?,
    @field:SerializedName("timezone") val timezone: String?,
    @field:SerializedName("createdAt") val createdAt: String?,
    @field:SerializedName("updatedAt") val updatedAt: String?,
) : BaseModel()