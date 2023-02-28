package com.example.parking.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.parking.callback.BaseViewInterface

abstract class DBSBaseViewBindingActivity<B : ViewBinding> : BaseCompatActivity(), BaseViewInterface {
    lateinit var binding: B
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
    }

    abstract fun getViewBinding(): B

    override fun getLifeCycleScope(): LifecycleCoroutineScope = lifecycleScope

    override fun getContext(): Context? = this

    override fun getRootView(): View? = binding.root
}