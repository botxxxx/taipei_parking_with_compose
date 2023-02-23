package com.example.parking.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parking.api.model.TCMSV_003_Rs

@BindingAdapter("chargeItems")
fun bindRecyclerViewWithDataItemList(recyclerView: RecyclerView, dataItemList: List<TCMSV_003_Rs>?) {
    dataItemList?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is ChargeAdapter -> submitList(it)
            }
        }
    }
}