package com.example.parking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.parking.R
import com.example.parking.adapters.ChargeAdapter
import com.example.parking.api.data.Parking
import com.example.parking.api.data.TimeZone
import com.example.parking.api.data.UPDATE_001_Rq
import com.example.parking.callback.ChooseTimeZoneHandler
import com.example.parking.databinding.FragmentEntryBinding
import com.example.parking.fragment.BaseViewBindingFragment
import com.example.parking.utils.DialogUtils
import com.example.parking.utils.Loading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryFragment : BaseViewBindingFragment<FragmentEntryBinding>(), ChooseTimeZoneHandler {

    private val viewModel: EntryViewModel by viewModels()
    private val args: EntryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setView()
    }

    private fun setView() {
        binding.rvList.apply {
            adapter = ChargeAdapter()
        }
        binding.mbtnTime.setOnClickListener {
            ChooseDialog(this).show(this.parentFragmentManager, null)
        }
        viewModel.apply {
            parkingDescLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onSuccess()
                }
            }
            parkingAvailableLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onSuccess()
                }
            }
            onFailureLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onError()
                    clearResponse()
                }
            }
            updateLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onSuccessDialog()
                    Loading.hide()
                }
            }
        }
        viewModel.getJson(this@EntryFragment)
    }

    private fun onSuccessDialog() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_hint),
            msg = resources.getString(R.string.common_text_success),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
        )
    }

    private fun onSuccess() {
        val parkingList: MutableList<Parking> = mutableListOf()
        val descList = viewModel.parkingDescLiveData.value?.data?.park
        val availableList = viewModel.parkingAvailableLiveData.value?.data?.park
        if (descList != null && availableList != null) {
            for (item in descList) {
                for (check in availableList) {
                    if (item.id == check.id) {
                        parkingList.add(Parking(item.id, item.getDesc(), check.getAvl()))
                        break
                    }
                }
            }
            Loading.hide()
            (binding.rvList.adapter as ChargeAdapter).submitList(parkingList)
        }
    }

    private fun onError() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_error_msg),
            msg = resources.getString(R.string.common_text_unknown_fail),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
            rightButtonListener = {
                findNavController().popBackStack()
            }
        )
    }

    override fun bindingCallback(): (LayoutInflater, ViewGroup?) -> FragmentEntryBinding = { layoutInflater, viewGroup ->
        FragmentEntryBinding.inflate(layoutInflater, viewGroup, false)
    }

    override fun getPhone(): String {
        return args.login?.phone ?: "未設定電話"
    }

    override fun onTimeZoneChange(): (TimeZone) -> Unit {
        return { _ ->
            val sessionToken = args.login?.sessionToken
            val objectId = args.login?.objectId
            val updateAt = args.login?.updatedAt
            val update = UPDATE_001_Rq(sessionToken, objectId, null, updateAt)
            viewModel.updateUser(update, this)
        }
    }
}