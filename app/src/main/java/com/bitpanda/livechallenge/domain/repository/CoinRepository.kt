package com.bitpanda.livechallenge.domain.repository

import arrow.core.Either
import com.bitpanda.livechallenge.domain.models.AppError
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.models.Rate
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getAssets(): Either<AppError, List<Asset>>

    suspend fun getRates(): Either<AppError, List<Rate>>

    suspend fun getUpdates(): Flow<List<Asset>>

    suspend fun disconnect()
}
