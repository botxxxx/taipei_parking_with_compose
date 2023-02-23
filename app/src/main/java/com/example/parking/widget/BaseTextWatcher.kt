package com.example.parking.widget

import android.text.Editable
import android.text.TextWatcher

abstract class BaseTextWatcher : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        editable?.let {
            afterTextChangedFinished(it)
        }
    }

    abstract fun afterTextChangedFinished(editable: Editable)
}