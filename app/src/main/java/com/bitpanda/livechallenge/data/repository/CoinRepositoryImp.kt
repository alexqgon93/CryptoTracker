package com.bitpanda.livechallenge.data.repository

import arrow.core.Either
import com.bitpanda.livechallenge.data.AssetsMapper
import com.bitpanda.livechallenge.data.RatesMapper
import com.bitpanda.livechallenge.data.remote.api.CryptoCoroutinesApi
import com.bitpanda.livechallenge.data.utils.mapResult
import com.bitpanda.livechallenge.data.utils.tryCall
import com.bitpanda.livechallenge.domain.models.AppError
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.models.Rate
import com.bitpanda.livechallenge.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImp @Inject constructor(
    private val cryptoApi: CryptoCoroutinesApi,
    private val assetsMapper: AssetsMapper,
    private val ratesMapper: RatesMapper,
) : CoinRepository {

    override suspend fun getAssets(): Either<AppError, List<Asset>> =
        tryCall { cryptoApi.getAsset() }.mapResult { asset -> assetsMapper.map(input = asset.data) }

    override suspend fun getRates(): Either<AppError, List<Rate>> =
        tryCall { cryptoApi.getRates() }.mapResult { rates -> ratesMapper.map(input = rates.data) }
}