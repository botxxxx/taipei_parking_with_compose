package com.example.parking.api

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.parking.api.data.*
import com.example.parking.db.ParkingDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class EntryRepository @Inject constructor(
    @Named("Parking") private val json: Retrofit,
    private val parkingDao: ParkingDao,
) {
    private val jsonUrlApi = json.create(JsonService::class.java)

    @WorkerThread
    fun loadParking(onStart: () -> Unit, onCompletion: () -> Unit, onError: () -> Unit): Flow<List<Parking>> = flow {
        val parking: List<Parking> = parkingDao.getParkingList()
        if (parking.isEmpty()) {
            try {
                val descRS = jsonUrlApi.getParkingDesc()
                val avlRs = jsonUrlApi.getParkingAvailable()
                val parkingRs = getParkingFromResult(descRS, avlRs)
                parkingDao.insertParkingList(parkingRs)
                emit(parkingRs)
            } catch (_: Exception) {
                onError()
            }
        } else {
            emit(parking)
        }
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)

    private fun getParkingFromResult(descRs: DESC_001_Rs, avlRs: AVL_001_Rs): List<Parking> {
        val parkingList: MutableList<Parking> = mutableListOf()
        val descList = descRs.data?.park
        val availableList = avlRs.data?.park
        if (descList != null && availableList != null) {
            for (desc in descList) {
                for (available in availableList) {
                    if (desc.id == available.id) {
                        parkingList.add(getParking(desc, available))
                        break
                    }
                }
            }
        }
        Log.e("parkingList", "$parkingList")
        return parkingList
    }

    private fun getParking(desc: DESC_003_Rs, available: AVL_003_Rs): Parking {
        desc.run {
            available.run {
                return Parking(
                    id, area, name, type, type2,
                    summary, address, tel, payex, tw97x, tw97y,
                    totalcar, totalmotor, totalbike, totalbus,
                    availablecar, availablemotor, availablebus
                )
            }
        }
    }
}
