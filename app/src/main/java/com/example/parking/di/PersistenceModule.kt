package com.example.parking.di

import android.app.Application
import androidx.room.Room
import com.example.parking.R
import com.example.parking.db.AppDatabase
import com.example.parking.db.ParkingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room
            .databaseBuilder(
                application,
                AppDatabase::class.java,
                application.getString(R.string.database)
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideParkingDao(appDatabase: AppDatabase): ParkingDao {
        return appDatabase.posterDao()
    }
}