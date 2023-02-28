package com.example.parking.api.data

import com.example.parking.api.model.BaseModel
import com.google.gson.annotations.SerializedName

data class Parking(
    @field:SerializedName("id") val id: String = "001",
    val desc: Desc?,
    val available: Available?,
) : BaseModel()

data class Desc(
    @field:SerializedName("area") val area: String?,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("type") val type: String?,
    @field:SerializedName("type2") val type2: String?,
    @field:SerializedName("summary") val summary: String?,
    @field:SerializedName("address") val address: String?,
    @field:SerializedName("tel") val tel: String?,
    @field:SerializedName("payex") val payex: String?,
    @field:SerializedName("tw97x") val tw97x: String?,
    @field:SerializedName("tw97y") val tw97y: String?,
    @field:SerializedName("totalcar") val totalcar: String?,
    @field:SerializedName("totalmotor") val totalmotor: String?,
    @field:SerializedName("totalbike") val totalbike: String?,
    @field:SerializedName("totalbus") val totalbus: String?,
) : BaseModel()

data class Available(
    @field:SerializedName("availablecar") val availablecar: String?,
    @field:SerializedName("availablemotor") val availablemotor: String?,
    @field:SerializedName("availablebus") val availablebus: String?,
    @field:SerializedName("ChargeStation") val ChargeStation: AVL_004_Rs?,
)