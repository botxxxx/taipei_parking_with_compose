package com.example.parking.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.example.parking.databinding.LoadingViewBinding

object Loading {

    var isLoading = false
    private var popupLoading: PopupWindow? = null
    private const val POPUP_LOCATION_OFFSET_X = 0
    private const val POPUP_LOCATION_OFFSET_Y = 0

    fun show(view: View?) {
        popupLoading?.dismiss()
        if (view?.isAttachedToWindow == true) {
            isLoading = true
            popupLoading = PopupWindow(
                LoadingViewBinding.inflate(LayoutInflater.from(view.context)).root,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            ).also { it.showAtLocation(view, Gravity.CENTER, POPUP_LOCATION_OFFSET_X, POPUP_LOCATION_OFFSET_Y) }
        }
    }

    fun hide() {
        popupLoading?.dismiss()
        isLoading = false
        popupLoading = null
    }
}