package com.example.parking.api.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.Serializable

open class BaseModel : Serializable {
    override fun toString(): String {
        return getGson().toJson(this)
    }

    private fun getGson(): Gson {
        return GsonBuilder().setLenient().setPrettyPrinting().create()
    }
}