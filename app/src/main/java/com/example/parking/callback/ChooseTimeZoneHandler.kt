package com.example.parking.callback

import com.example.parking.api.data.TimeZone

interface ChooseTimeZoneHandler {
    fun getPhone(): String
    fun onTimeZoneChange(): (TimeZone) -> Unit
}