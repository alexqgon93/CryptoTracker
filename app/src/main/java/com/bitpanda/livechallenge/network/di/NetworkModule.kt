package com.bitpanda.livechallenge.network.di

import com.bitpanda.livechallenge.BuildConfig
import com.bitpanda.livechallenge.network.api.CryptoCoroutinesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://rest.coincap.io/v3/"

    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request =
                    original
                        .newBuilder()
                        .header("Authorization", "Bearer ${BuildConfig.api_key}")
                        .build()
                chain.proceed(request)
            }.addInterceptor(httpLoggingInterceptor)
            .callTimeout(timeout = 10, unit = TimeUnit.SECONDS)
            .connectTimeout(timeout = 10, unit = TimeUnit.SECONDS)
            .writeTimeout(timeout = 10, unit = TimeUnit.SECONDS)
            .readTimeout(timeout = 10, unit = TimeUnit.SECONDS)
            .build()

    @Provides
    fun providesMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun providesRetrofit(
        baseUrl: String,
        converterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    fun providesCryptoCoroutines(
        converterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ): CryptoCoroutinesApi = providesRetrofit(
        baseUrl = BASE_URL,
        converterFactory = converterFactory,
        okHttpClient = okHttpClient
    ).create(CryptoCoroutinesApi::class.java)
}
