package com.example.parking.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.api.MainRepository
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.api.model.BaseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    val userData: MutableLiveData<LOGIN_001_Rs?> = MutableLiveData()
    val onFailure: MutableLiveData<BaseModel?> = MutableLiveData()
    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    fun doLogin(login: LOGIN_001_Rq) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendLoginRequest(
                login,
                onStart = { _isLoading.value = true },
                onCompletion = {
                    _isLoading.value = false
                },
                onError = {
                    _isLoading.value = false
                    onFailure.postValue(BaseModel())
                    Log.e("error", "getLogin error")
                }
            ).collect {
                userData.postValue(it)
            }
        }
    }
}