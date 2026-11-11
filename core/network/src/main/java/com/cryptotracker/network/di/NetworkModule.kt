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

/**
 * Hilt bindings for the HTTP stack: logging, OkHttp, Moshi and the Retrofit API.
 *
 * Bindings are declared bottom-up, from the smallest building blocks to [CryptoCoroutinesApi].
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://rest.coincap.io/v3/"
    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val AUTHORIZATION_SCHEME = "Bearer"
    private const val TIMEOUT_SECONDS = 10L

    @Provides
    fun providesMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    /** Logs basic request lines in debug builds only and never prints the bearer token. */
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            redactHeader(AUTHORIZATION_HEADER)
            level =
                when {
                    BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BASIC
                    else -> HttpLoggingInterceptor.Level.NONE
                }
        }

    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        createOkHttpClient(
            apiKey = BuildConfig.API_KEY,
            httpLoggingInterceptor = httpLoggingInterceptor,
        )

    /**
     * Builds the client; the bearer header is attached only when [apiKey] is not blank.
     */
    internal fun createOkHttpClient(
        apiKey: String,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val authorizedRequest =
                    when {
                        apiKey.isBlank() -> request
                        else ->
                            request
                                .newBuilder()
                                .header(AUTHORIZATION_HEADER, "$AUTHORIZATION_SCHEME $apiKey")
                                .build()
                    }
                chain.proceed(authorizedRequest)
            }.addInterceptor(httpLoggingInterceptor)
            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    @Provides
    fun providesRetrofit(
        baseUrl: String,
        converterFactory: MoshiConverterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
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
