package com.example.parking.callback

import android.content.Context
import android.view.View
import kotlinx.coroutines.CoroutineScope

interface BaseViewInterface {
    fun getContext(): Context?
    fun getRootView(): View?
    fun getLifeCycleScope(): CoroutineScope
}