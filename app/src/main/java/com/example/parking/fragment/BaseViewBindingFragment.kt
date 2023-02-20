package com.example.parking.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.parking.callback.ViewBindingInterface

abstract class BaseViewBindingFragment<B : ViewBinding> : BaseFragment(), ViewBindingInterface {

    private var _binding: B? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun getViewBinding(): ViewBinding {
        return binding
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingCallback().invoke(inflater, container)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun bindingCallback(): (LayoutInflater, ViewGroup?) -> B

    override fun getRootView(): View? = binding.root
}