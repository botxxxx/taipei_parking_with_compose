package com.example.parking.main

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.api.EntryRepository
import com.example.parking.api.data.AVL_001_Rs
import com.example.parking.api.data.DESC_001_Rs
import com.example.parking.api.data.Parking
import com.example.parking.api.model.BaseCallBack
import com.example.parking.api.model.BaseModel
import com.example.parking.utils.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val repository: EntryRepository
) : ViewModel() {
    val parkingDesc: MutableLiveData<DESC_001_Rs?> = MutableLiveData()
    val parkingAvailable: MutableLiveData<AVL_001_Rs?> = MutableLiveData()
    val parkingDetail: MutableLiveData<MutableList<Parking>> = MutableLiveData()
    val onFailure: MutableLiveData<BaseModel?> = MutableLiveData()

    fun getJson(view: View) {
        Log.e("request", "getJson()")
        Loading.show(view)
        getDesc()
        getAvailable()
    }

    private fun getDesc() {
        repository.getParkingDescRequest(object : BaseCallBack<DESC_001_Rs>(viewModelScope) {
            override fun onResponse(response: DESC_001_Rs) {
                Log.e("response", "success")
                parkingDesc.postValue(response)
            }

            override fun onFailure() {
                Log.e("response", "fail")
                onFailure.postValue(BaseModel())
            }
        })
    }

    private fun getAvailable() {
        repository.getParkingAvailableRequest(object : BaseCallBack<AVL_001_Rs>(viewModelScope) {
            override fun onResponse(response: AVL_001_Rs) {
                Log.e("response", "success")
                parkingAvailable.postValue(response)
            }

            override fun onFailure() {
                Log.e("response", "fail")
                onFailure.postValue(BaseModel())
            }
        })
    }
}