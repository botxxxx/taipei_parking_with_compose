package com.example.parking.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parking.api.data.Parking

@Database(entities = [Parking::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun posterDao(): ParkingDao
}