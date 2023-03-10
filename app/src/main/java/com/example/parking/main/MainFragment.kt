package com.example.parking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.parking.R
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.databinding.FragmentMainBinding
import com.example.parking.fragment.BaseViewBindingFragment
import com.example.parking.utils.DialogUtils
import com.example.parking.widget.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseViewBindingFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setView()
    }

    private fun setView() {
        viewModel.apply {
            userLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    navigateToEntry(it, binding.root)
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

    private fun navigateToEntry(result: LOGIN_001_Rs?, view: View) {
        val direction = MainFragmentDirections.actionToEntry(result)
        view.findNavController().navigate(direction)
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