package com.example.parking.api.data

import com.example.parking.api.model.BaseModel
import com.google.gson.annotations.SerializedName

data class Parking(
    @field:SerializedName("id") val id: String = "001",
    val desc: Desc?,
    val available: Available?,
) : BaseModel() {
    private fun getCar() = if (!desc?.totalcar.equals("0")) {
        "汽車: ${available?.availablecar}/${desc?.totalcar}"
    } else ""

    private fun getMotor() = if (!desc?.totalmotor.equals("0")) {
        "\n機車: ${available?.availablemotor}/${desc?.totalmotor}"
    } else ""

    fun getAvailableCar() = "(可用/車位) ${getCar()} ${getMotor()}"
    fun getAvailableCarExt() = "${getCar()}${getMotor()}"
}

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
) : BaseModel() {
    fun getTotalCar() = "汽車: $totalcar 機車: $totalmotor"
    fun getTelPhone() = "電話: ${tel?.let { "02-$tel" } ?: "無"}"
}

data class Available(
    @field:SerializedName("availablecar") val availablecar: String?,
    @field:SerializedName("availablemotor") val availablemotor: String?,
    @field:SerializedName("availablebus") val availablebus: String?,
    @field:SerializedName("ChargeStation") val ChargeStation: AVL_004_Rs?,
) : BaseModel() {
    fun getAvailableCar() = "汽車:$availablecar 機車:$availablemotor"
}

object Park {
    val mockDesc = Desc(
        "area", "name", "", "",
        "summary", "address", "tel",
        "", "", "",
        "car", "motor", "bike", "bus"
    )
    val mockAvailable = Available("car", "motor", "bus", null)
    val mockParking = Parking(
        id = "",
        desc = mockDesc,
        available = mockAvailable
    )
}