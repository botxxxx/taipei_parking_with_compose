package com.example.parking.di

import com.example.parking.api.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    @Named("Login")
    fun provideLoginService(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://noodoe-app-development.web.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @Named("Parking")
    fun provideParkingService(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://tcgbusfs.blob.core.windows.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}