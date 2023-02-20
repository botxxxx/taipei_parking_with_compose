package com.example.parking.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.parking.R
import com.example.parking.databinding.FragmentMainBinding
import com.example.parking.fragment.BaseViewBindingFragment

class MainFragment : BaseViewBindingFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun bindingCallback(): (LayoutInflater, ViewGroup?) -> FragmentMainBinding = { layoutInflater, viewGroup ->
        FragmentMainBinding.inflate(layoutInflater, viewGroup, false)
    }
}