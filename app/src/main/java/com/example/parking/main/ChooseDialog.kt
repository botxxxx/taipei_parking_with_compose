package com.example.parking.main

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.parking.R
import com.example.parking.api.data.TimeZone
import com.example.parking.callback.ChooseTimeZoneHandler
import com.example.parking.databinding.ChooseDialogBinding
import com.example.parking.fragment.BaseBottomSheetDialogFragment

class ChooseDialog(private val handler: ChooseTimeZoneHandler) : BaseBottomSheetDialogFragment<ChooseDialogBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogCircle)
        initView(handler)
        setEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(handler: ChooseTimeZoneHandler) {
        val languageList = listOf(
            TimeZone.TW, TimeZone.CN, TimeZone.EN, TimeZone.JP, TimeZone.KO, TimeZone.ES, TimeZone.ID, TimeZone.TH, TimeZone.VI
        )
        binding.run {
            tvEmail.text = "電話: ${handler.getPhone()}"
            rvTime.adapter = ChooseTimeZoneAdapter(languageList, onItemClick = { langInfo ->
                handler.onTimeZoneChange().invoke(langInfo)
                dialog?.dismiss()
            })
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                setDrawable(ColorDrawable(R.drawable.divider_common))
            }
            rvTime.addItemDecoration(itemDecoration)
        }
    }

    override fun onDestroyView() {
        binding.rvTime.adapter = null
        super.onDestroyView()
    }

    private fun setEvent() {
        binding.apply {
            tvDismiss.setOnClickListener { dialog?.dismiss() }
        }
    }

    override fun bindingCallback(): (LayoutInflater, ViewGroup?) -> ChooseDialogBinding =
        { layoutInflater, viewGroup -> ChooseDialogBinding.inflate(layoutInflater, viewGroup, false) }
}