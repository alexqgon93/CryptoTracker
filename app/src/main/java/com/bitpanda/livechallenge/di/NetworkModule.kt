package com.bitpanda.livechallenge.di

import com.bitpanda.livechallenge.data.remote.CryptoCoroutinesApi
import com.bitpanda.livechallenge.data.remote.CryptoRxApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    @Provides
    fun provideRetrofit(json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.coincap.io/v2/")
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun provideCryptoCoroutinesApi(retrofit: Retrofit): CryptoCoroutinesApi {
        return retrofit.create()
    }

    @Provides
    fun provideCryptoRxApi(retrofit: Retrofit): CryptoRxApi {
        return retrofit.create()
    }
}