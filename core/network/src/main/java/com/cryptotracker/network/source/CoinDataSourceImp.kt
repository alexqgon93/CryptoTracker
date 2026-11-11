package com.cryptotracker.network.source

import arrow.core.Either
import com.cryptotracker.network.api.CryptoCoroutinesApi
import com.cryptotracker.network.responses.NetworkAssetResponse
import com.cryptotracker.network.responses.NetworkRateResponse
import com.cryptotracker.network.utils.Failure
import com.cryptotracker.network.utils.tryCall
import javax.inject.Inject

class CoinDataSourceImp
    @Inject
    constructor(
        private val cryptoApi: CryptoCoroutinesApi,
    ) : CoinDataSource {
        override suspend fun getAssets(): Either<Failure, NetworkAssetResponse> =
            tryCall {
                cryptoApi.getAsset()
            }

        override suspend fun getRates(): Either<Failure, NetworkRateResponse> =
            tryCall {
                cryptoApi.getRates()
            }
    }
