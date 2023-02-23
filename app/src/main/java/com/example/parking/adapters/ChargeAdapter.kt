package com.example.parking.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.parking.api.model.TCMSV_003_Rs

class ChargeAdapter : BaseDataBindingAdapter<TCMSV_003_Rs>(ChargeDiffCallback()) {

    private class ChargeDiffCallback : DiffUtil.ItemCallback<TCMSV_003_Rs>() {
        override fun areItemsTheSame(oldItem: TCMSV_003_Rs, newItem: TCMSV_003_Rs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TCMSV_003_Rs, newItem: TCMSV_003_Rs): Boolean {
            return oldItem == newItem
        }
    }
}