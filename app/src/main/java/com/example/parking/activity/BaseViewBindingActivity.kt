package com.example.parking.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class DBSBaseViewBindingActivity<B : ViewBinding> : BaseCompatActivity() {
    lateinit var binding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
    }

    abstract fun getViewBinding(): B
}