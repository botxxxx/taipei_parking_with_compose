package com.example.parking.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parking.api.data.Parking

@BindingAdapter("chargeItems")
fun bindRecyclerViewWithDataItemList(recyclerView: RecyclerView, dataItemList: List<Parking>?) {
    dataItemList?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is ChargeAdapter -> submitList(it)
            }
        }
    }
}