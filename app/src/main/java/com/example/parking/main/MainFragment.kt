package com.example.parking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.parking.R
import com.example.parking.api.NetworkService
import com.example.parking.api.model.LOGIN_001_Rq
import com.example.parking.callback.BaseViewInterface
import com.example.parking.databinding.FragmentMainBinding
import com.example.parking.fragment.BaseViewBindingFragment
import com.example.parking.utils.DialogUtils

class MainFragment : BaseViewBindingFragment<FragmentMainBinding>(), BaseViewInterface {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        NetworkService.init()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java].apply {
            userLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onServiceAPISuccess()
                    clearResponse()
                }
            }
            onFailureLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onServiceAPIError()
                    clearResponse()
                }
            }
            this.getServiceStateList(this@MainFragment, LOGIN_001_Rq())
        }
    }

    private fun onServiceAPISuccess() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_hint),
            msg = resources.getString(R.string.common_text_success),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
        )
    }

    private fun onServiceAPIError() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_error_msg),
            msg = resources.getString(R.string.common_login_failure),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
        )
    }

    override fun bindingCallback(): (LayoutInflater, ViewGroup?) -> FragmentMainBinding = { layoutInflater, viewGroup ->
        FragmentMainBinding.inflate(layoutInflater, viewGroup, false)
    }
}