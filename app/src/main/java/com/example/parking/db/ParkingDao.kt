package com.example.parking.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parking.api.data.Parking

@Dao
interface ParkingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkingList(posters: List<Parking>)

    @Query("SELECT * FROM Parking")
    suspend fun getParkingList(): List<Parking>
}