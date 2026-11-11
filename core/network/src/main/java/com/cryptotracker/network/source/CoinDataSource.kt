package com.cryptotracker.network.source

import arrow.core.Either
import com.cryptotracker.network.responses.NetworkAssetResponse
import com.cryptotracker.network.responses.NetworkRateResponse
import com.cryptotracker.network.utils.Failure

interface CoinDataSource {
    suspend fun getAssets(): Either<Failure, NetworkAssetResponse>

    suspend fun getRates(): Either<Failure, NetworkRateResponse>
}
