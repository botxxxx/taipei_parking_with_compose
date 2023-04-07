package com.example.parking.api.data

import com.example.parking.api.model.BaseModel
import com.google.gson.annotations.SerializedName

data class AVL_001_Rs(
    @field:SerializedName("data") val data: AVL_002_Rs?,
) : BaseModel()

data class AVL_002_Rs(
    @field:SerializedName("UPDATETIME") val time: String?,
    @field:SerializedName("park") val park: List<AVL_003_Rs>?
) : BaseModel()

data class AVL_003_Rs(
    @field:SerializedName("id") val id: String = "001",
    @field:SerializedName("availablecar") val availablecar: String = "",
    @field:SerializedName("availablemotor") val availablemotor: String = "",
    @field:SerializedName("availablebus") val availablebus: String = "",
    @field:SerializedName("ChargeStation") val ChargeStation: AVL_004_Rs?,
) : BaseModel()

data class AVL_004_Rs(
    @field:SerializedName("scoketStatusList") val scoketStatusList: List<DESC_005_Rs>?
) : BaseModel()

data class DESC_005_Rs(
    @field:SerializedName("spot_abrv") val spot_abrv: String?,
    @field:SerializedName("spot_status") val spot_status: String?,
) : BaseModel()