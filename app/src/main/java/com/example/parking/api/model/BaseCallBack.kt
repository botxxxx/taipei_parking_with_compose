package com.example.parking.api.model

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseCallBack<T>(val lifecycleScope: CoroutineScope) {
    /**
     * Reduce the chances of using GlobalScope, but also prevent normal function break down !!
     */
    fun getResponse(response: T) {
        try {
            lifecycleScope.launch(Dispatchers.IO) {
                onResponse(response)
            }
        } catch (ex: Exception) {
            Log.e("error", "Callback fail: ${Log.getStackTraceString(ex)}")
            onFailure()
        }
    }

    abstract fun onResponse(response: T)
    abstract fun onFailure()
}