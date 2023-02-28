package com.example.parking.callback

import com.example.parking.adapters.BaseDataBindingViewHolder
import com.example.parking.databinding.ChargeItemBinding

interface BaseAdapterHandler<T> {
    fun setViewHolder(binding: ChargeItemBinding): BaseDataBindingViewHolder<T>
}