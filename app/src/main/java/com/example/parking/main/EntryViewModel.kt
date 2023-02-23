package com.example.parking.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parking.api.NetworkService
import com.example.parking.api.model.BaseCallBack
import com.example.parking.api.model.BaseModel
import com.example.parking.api.model.TCMSV_001_Rs
import com.example.parking.callback.BaseViewInterface
import com.example.parking.utils.Loading

class EntryViewModel : ViewModel() {
    val chargeLiveData: MutableLiveData<TCMSV_001_Rs?> = MutableLiveData()
    val onFailureLiveData: MutableLiveData<BaseModel?> = MutableLiveData()
    fun clearResponse() {
        chargeLiveData.value = null
        onFailureLiveData.value = null
        Loading.hide()
    }

    fun getJson(baseViewInterface: BaseViewInterface) {
        clearResponse()
        Log.e("request", "getJson()")
        Loading.show(baseViewInterface.getRootView())
        NetworkService.getJsonRequest(object : BaseCallBack<TCMSV_001_Rs>(baseViewInterface) {
            override fun onResponse(response: TCMSV_001_Rs) {
                Log.e("response", "success")
                chargeLiveData.postValue(response)
            }

            override fun onFailure() {
                Log.e("response", "fail")
                onFailureLiveData.postValue(BaseModel())
            }
        })
    }
}