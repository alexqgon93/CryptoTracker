package com.cryptotracker.domain.repository

import arrow.core.Either
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.domain.models.Asset
import com.cryptotracker.domain.models.Rate

interface CoinRepository {
    suspend fun getAssets(): Either<AppError, List<Asset>>

    suspend fun getRates(): Either<AppError, List<Rate>>
}
