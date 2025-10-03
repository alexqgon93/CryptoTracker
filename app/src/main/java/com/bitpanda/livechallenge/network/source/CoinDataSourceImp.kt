package com.bitpanda.livechallenge.network.source

import arrow.core.Either
import com.bitpanda.livechallenge.network.api.CryptoCoroutinesApi
import com.bitpanda.livechallenge.network.responses.NetworkAssetResponse
import com.bitpanda.livechallenge.network.responses.NetworkRateResponse
import com.bitpanda.livechallenge.network.utils.Failure
import com.bitpanda.livechallenge.network.utils.tryCall
import javax.inject.Inject

class CoinDataSourceImp @Inject constructor(private val cryptoApi: CryptoCoroutinesApi) :
    CoinDataSource {
    override suspend fun getAssets(): Either<Failure, NetworkAssetResponse> = tryCall {
        cryptoApi.getAsset()
    }

    override suspend fun getRates(): Either<Failure, NetworkRateResponse> = tryCall {
        cryptoApi.getRates()
    }
}
