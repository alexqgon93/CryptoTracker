package com.bitpanda.livechallenge.network.source

import arrow.core.Either
import com.bitpanda.livechallenge.network.responses.NetworkAssetResponse
import com.bitpanda.livechallenge.network.responses.NetworkRateResponse
import com.bitpanda.livechallenge.network.utils.Failure

interface CoinDataSource {
    suspend fun getAssets(): Either<Failure, NetworkAssetResponse>

    suspend fun getRates(): Either<Failure, NetworkRateResponse>
}
