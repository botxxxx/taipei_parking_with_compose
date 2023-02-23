package com.example.parking.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.google.android.material.textfield.TextInputEditText

class TextInputEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = androidx.constraintlayout.widget.R.attr.editTextStyle) :
    TextInputEditText(context, attrs, defStyleAttr) {

    private var onKeyPreImeListener: ((keyCode: Int, event: KeyEvent?) -> Boolean)? = null

    fun setOnKeyPreImeListener(onKeyPreImeListener: ((keyCode: Int, event: KeyEvent?) -> Boolean)?) {
        this.onKeyPreImeListener = onKeyPreImeListener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        super.onKeyPreIme(keyCode, event)
        if (keyCode == KeyEvent.KEYCODE_BACK) {   //收起鍵盤
            clearFocus()
            return true
        }
        return false
    }
}