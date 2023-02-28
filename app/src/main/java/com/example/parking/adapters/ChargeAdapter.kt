package com.example.parking.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.parking.api.data.Parking
import com.example.parking.databinding.ChargeItemBinding

class ChargeAdapter : BaseDataBindingAdapter<Parking>(ChargeDiffCallback()) {

    override fun setViewHolder(binding: ChargeItemBinding): BaseDataBindingViewHolder<Parking> {
        return ParkingViewHolder(binding)
    }

    private class ParkingViewHolder<T>(binding: ChargeItemBinding) : BaseDataBindingViewHolder<T>(binding) {
        init {
            binding.root.setOnClickListener {
//                itemView.findFragment<EntryFragment>()navigateToDetail(binding.attr, it)
            }
        }
    }

    private class ChargeDiffCallback : DiffUtil.ItemCallback<Parking>() {
        override fun areItemsTheSame(oldItem: Parking, newItem: Parking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Parking, newItem: Parking): Boolean {
            return oldItem == newItem
        }
    }
}