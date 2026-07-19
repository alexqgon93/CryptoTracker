package com.cryptotracker.network.di

import com.cryptotracker.core.network.BuildConfig
import com.cryptotracker.network.api.CryptoCoroutinesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://rest.coincap.io/v3/"
    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val AUTHORIZATION_SCHEME = "Bearer"

    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        createOkHttpClient(
            apiKey = BuildConfig.API_KEY,
            httpLoggingInterceptor = httpLoggingInterceptor,
        )

    internal fun createOkHttpClient(
        apiKey: String,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                if (apiKey.isNotBlank()) {
                    requestBuilder.header(AUTHORIZATION_HEADER, "$AUTHORIZATION_SCHEME $apiKey")
                }
                chain.proceed(requestBuilder.build())
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
        HttpLoggingInterceptor().apply {
            redactHeader(AUTHORIZATION_HEADER)
            level =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
        }

    @Provides
    fun providesRetrofit(
        baseUrl: String,
        converterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .build()

    @Provides
    fun providesCryptoCoroutines(
        converterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): CryptoCoroutinesApi =
        providesRetrofit(
            baseUrl = BASE_URL,
            converterFactory = converterFactory,
            okHttpClient = okHttpClient,
        ).create(CryptoCoroutinesApi::class.java)
}
