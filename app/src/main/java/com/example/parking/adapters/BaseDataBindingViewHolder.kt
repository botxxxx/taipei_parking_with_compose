package com.example.parking.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.parking.BR
import com.example.parking.callback.ViewHolderHandler

open class BaseDataBindingViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root), ViewHolderHandler<T> {
    override fun bind(item: T) {
        binding.setVariable(BR.park, item)
        binding.executePendingBindings()
    }
}