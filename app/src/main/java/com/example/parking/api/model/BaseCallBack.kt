package com.example.parking.api.model

import android.util.Log
import com.example.parking.callback.BaseViewInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseCallBack<T>(val baseViewInterface: BaseViewInterface) {
    /**
     * Reduce the chances of using GlobalScope, but also prevent normal function break down !!
     */
    private val safeCoroutineScope = baseViewInterface.getLifeCycleScope()

    fun getResponse(response: T) {
        try {
            safeCoroutineScope.launch(Dispatchers.IO) {
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