package com.example.parking.callback

interface ViewHolderHandler<T> {
    fun bind(item: T)
}