package com.example.parking.main

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.api.MainRepository
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.api.model.BaseCallBack
import com.example.parking.api.model.BaseModel
import com.example.parking.utils.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    val userData: MutableLiveData<LOGIN_001_Rs?> = MutableLiveData()
    val onFailure: MutableLiveData<BaseModel?> = MutableLiveData()

    fun getLogin(login: LOGIN_001_Rq, rootView: View) {
        Loading.hide()
        Log.e("request", "$login")
        Loading.show(rootView)
        repository.sendLoginRequest(login, object : BaseCallBack<LOGIN_001_Rs>(viewModelScope) {
            override fun onResponse(response: LOGIN_001_Rs) {
                Log.e("response", "success")
                userData.postValue(response)
            }

            override fun onFailure() {
                Log.e("response", "fail")
                onFailure.postValue(BaseModel())
            }
        })
    }
}