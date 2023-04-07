package com.example.parking.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parking.api.EntryRepository
import com.example.parking.api.data.Parking
import com.example.parking.api.model.BaseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    repository: EntryRepository
) : ViewModel() {
    val onFailure: MutableLiveData<BaseModel?> = MutableLiveData()
    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    val parkingList: Flow<List<Parking>> = repository.loadParking(
        onStart = { _isLoading.value = true },
        onCompletion = { _isLoading.value = false },
        onError = {
            _isLoading.value = false
            onFailure.postValue(BaseModel())
            Log.e("error", "getJson error")
        }
    )
}