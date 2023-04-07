package com.example.parking.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.api.DetailRepository
import com.example.parking.api.data.UPDATE_001_Rq
import com.example.parking.api.data.UPDATE_001_Rs
import com.example.parking.api.model.BaseCallBack
import com.example.parking.api.model.BaseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository
) : ViewModel() {
    val updateUser: MutableLiveData<UPDATE_001_Rs?> = MutableLiveData()
    val onFailure: MutableLiveData<BaseModel?> = MutableLiveData()

    fun updateUserData(update: UPDATE_001_Rq) {
        repository.sendUserUpdate(update, object : BaseCallBack<UPDATE_001_Rs>(viewModelScope) {
            override fun onResponse(response: UPDATE_001_Rs) {
                Log.e("response", "success")
                Log.e("response", "$response")
                updateUser.postValue(response)
            }

            override fun onFailure() {
                Log.e("response", "fail")
                onFailure.postValue(BaseModel())
            }
        })
    }
}