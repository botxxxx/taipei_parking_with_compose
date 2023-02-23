package com.example.parking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.parking.R
import com.example.parking.adapters.ChargeAdapter
import com.example.parking.databinding.FragmentEntryBinding
import com.example.parking.fragment.BaseViewBindingFragment
import com.example.parking.utils.DialogUtils

class EntryFragment : BaseViewBindingFragment<FragmentEntryBinding>() {

    private lateinit var viewModel: EntryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setView()
    }

    private fun setView() {
        binding.rvList.apply {
            adapter = ChargeAdapter()
        }
        viewModel = ViewModelProvider(this)[EntryViewModel::class.java].apply {
            chargeLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    (binding.rvList.adapter as ChargeAdapter).submitList(it.data?.park)
                    onSuccess()
                    clearResponse()
                }
            }
            onFailureLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onError()
                    clearResponse()
                }
            }
            this.getJson(this@EntryFragment)
        }
    }

    private fun onSuccess() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_hint),
            msg = resources.getString(R.string.common_text_success),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
        )
    }

    private fun onError() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_error_msg),
            msg = resources.getString(R.string.common_text_unknown_fail),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
        )
    }

    override fun bindingCallback(): (LayoutInflater, ViewGroup?) -> FragmentEntryBinding = { layoutInflater, viewGroup ->
        FragmentEntryBinding.inflate(layoutInflater, viewGroup, false)
    }

    companion object {
        fun newInstance() = EntryFragment()
    }
}