package com.example.parking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.parking.R
import com.example.parking.api.NetworkService
import com.example.parking.api.model.LOGIN_001_Rq
import com.example.parking.databinding.FragmentMainBinding
import com.example.parking.fragment.BaseViewBindingFragment
import com.example.parking.utils.ActivityUtils
import com.example.parking.utils.DialogUtils
import com.example.parking.widget.TextInputLayout

class MainFragment : BaseViewBindingFragment<FragmentMainBinding>() {

    private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        NetworkService.init()
        setView()
    }

    private fun setView() {
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
        }
        binding.apply {
            mbtnLogin.setOnClickListener {
                viewModel.getServiceStateList(LOGIN_001_Rq(tilUser.text, tilPwd.text), this@MainFragment)
            }
            tilPwd.setEndIconOnClickListener(object : TextInputLayout.EndIconOnClickCallback {
                override fun onClick(til: TextInputLayout, view: View) {
                    val inputTypeStateSwitchTo = when (til.getInputType()) {
                        TextInputLayout.INPUT_TYPE_TEXT_PASSWORD_MASK -> {
                            Pair(TextInputLayout.INPUT_TYPE_TEXT_PASSWORD, R.drawable.ic_eye_open_apple)
                        }
                        else -> Pair(TextInputLayout.INPUT_TYPE_TEXT_PASSWORD_MASK, R.drawable.ic_eye_close_apple)
                    }
                    val inputType = til.getTypeface() //保持隱碼為全型，避免被覆蓋
                    til.setInputType(inputTypeStateSwitchTo.first)
                    til.setCustomIcon(inputTypeStateSwitchTo.second)
                    til.putCursorToTextTail()
                    til.setTypeface(inputType)
                }
            })
        }
    }

    private fun onServiceAPISuccess() {
        ActivityUtils.replaceFragment(
            parentFragmentManager,
            R.id.container,
            EntryFragment.newInstance(),
            EntryFragment::class.simpleName
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

    companion object {
        fun newInstance() = MainFragment()
    }
}