package com.example.parking.api.data

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parking.api.model.BaseModel

@Entity
@Immutable
data class Parking(
    @PrimaryKey val id: String = "001",

    val area: String,
    val name: String,
    val type: String,
    val type2: String,
    val summary: String,
    val address: String,
    val tel: String,
    val payex: String,
    val tw97x: String,
    val tw97y: String,
    val totalcar: String,
    val totalmotor: String,
    val totalbike: String,
    val totalbus: String,

    val availablecar: String,
    val availablemotor: String,
    val availablebus: String,
) : BaseModel() {

    fun getDesc() = Desc(area, name, type, type2, summary, address, tel, payex, tw97x, tw97y, totalcar, totalmotor, totalbike, totalbus)
    fun getAvailable() = Available(availablecar, availablemotor, availablebus)

    private fun getCar() = if (!totalcar.equals("0")) {
        "汽車: ${availablecar}/${totalcar}"
    } else ""

    private fun getMotor() = if (!totalmotor.equals("0")) {
        "\n機車: ${availablemotor}/${totalmotor}"
    } else ""

    fun getAvailableCar() = "(可用/車位) ${getCar()} ${getMotor()}"
    fun getAvailableCarExt() = "${getCar()}${getMotor()}"


    companion object {
        fun mock() = Parking(
            "001",
            "area", "name", "", "",
            "summary", "address", "tel",
            "", "", "",
            "car", "motor", "bike", "bus",
            "car", "motor", "bus"
        )
    }
}

data class Desc(
    val area: String?,
    val name: String?,
    val type: String?,
    val type2: String?,
    val summary: String?,
    val address: String?,
    val tel: String?,
    val payex: String?,
    val tw97x: String?,
    val tw97y: String?,
    val totalcar: String?,
    val totalmotor: String?,
    val totalbike: String?,
    val totalbus: String?,
) : BaseModel() {
    fun getTotalCar() = "汽車: $totalcar 機車: $totalmotor"
    fun getTelPhone() = "電話: ${tel?.let { "02-$tel" } ?: "無"}"
}

data class Available(
    val availablecar: String?,
    val availablemotor: String?,
    val availablebus: String?,
) : BaseModel() {
    fun getAvailableCar() = "汽車:$availablecar 機車:$availablemotor"
}