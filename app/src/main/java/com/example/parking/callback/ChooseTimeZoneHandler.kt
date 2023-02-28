package com.example.parking.callback

import com.example.parking.api.data.TimeZone

interface ChooseTimeZoneHandler {
    fun getEmail(): String? = "電子郵件未設定"
    fun onTimeZoneChange(): (TimeZone) -> Unit
}