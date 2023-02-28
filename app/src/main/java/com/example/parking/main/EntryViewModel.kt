package com.example.parking.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parking.api.NetworkService
import com.example.parking.api.data.AVL_001_Rs
import com.example.parking.api.data.DESC_001_Rs
import com.example.parking.api.model.BaseCallBack
import com.example.parking.api.model.BaseModel
import com.example.parking.callback.BaseViewInterface
import com.example.parking.utils.Loading

class EntryViewModel : ViewModel() {
    val parkingDescLiveData: MutableLiveData<DESC_001_Rs?> = MutableLiveData()
    val parkingAvailableLiveData: MutableLiveData<AVL_001_Rs?> = MutableLiveData()
    val onFailureLiveData: MutableLiveData<BaseModel?> = MutableLiveData()
    fun clearResponse() {
        parkingDescLiveData.value = null
        parkingAvailableLiveData.value = null
        onFailureLiveData.value = null
        Loading.hide()
    }

    fun getJson(baseViewInterface: BaseViewInterface) {
        clearResponse()
        Log.e("request", "getJson()")
        Loading.show(baseViewInterface.getRootView())
        getDesc(baseViewInterface)
        getAvailable(baseViewInterface)
    }

    private fun getDesc(baseViewInterface: BaseViewInterface) {
        NetworkService.getParkingDescRequest(object : BaseCallBack<DESC_001_Rs>(baseViewInterface) {
            override fun onResponse(response: DESC_001_Rs) {
                Log.e("response", "success")
                parkingDescLiveData.postValue(response)
            }

            override fun onFailure() {
                Log.e("response", "fail")
                onFailureLiveData.postValue(BaseModel())
            }
        })
    }


    private fun getAvailable(baseViewInterface: BaseViewInterface) {
        NetworkService.getParkingAvailableRequest(object : BaseCallBack<AVL_001_Rs>(baseViewInterface) {
            override fun onResponse(response: AVL_001_Rs) {
                Log.e("response", "success")
                parkingAvailableLiveData.postValue(response)
            }

            override fun onFailure() {
                Log.e("response", "fail")
                onFailureLiveData.postValue(BaseModel())
            }
        })
    }
}