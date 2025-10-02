package com.bitpanda.livechallenge.data.repository

import arrow.core.Either
import com.bitpanda.livechallenge.data.mappers.AssetsMapper
import com.bitpanda.livechallenge.data.mappers.RatesMapper
import com.bitpanda.livechallenge.data.utils.mapResult
import com.bitpanda.livechallenge.domain.models.AppError
import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.models.Rate
import com.bitpanda.livechallenge.domain.repository.CoinRepository
import com.bitpanda.livechallenge.network.source.CoinDataSource
import com.bitpanda.livechallenge.network.source.WebSocketDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CoinRepositoryImp
@Inject
constructor(
    private val assetsDataSource: CoinDataSource,
    private val assetsMapper: AssetsMapper,
    private val ratesMapper: RatesMapper,
    private val datasource: WebSocketDataSource
) : CoinRepository {
    override suspend fun getAssets(): Either<AppError, List<Asset>> =
        assetsDataSource.getAssets().mapResult { asset -> assetsMapper.map(input = asset.data) }

    override suspend fun getRates(): Either<AppError, List<Rate>> =
        assetsDataSource.getRates().mapResult { rates -> ratesMapper.map(input = rates.data) }

    override suspend fun getUpdates(): Flow<List<Asset>> = datasource.connect()

    override suspend fun disconnect() = datasource.disconnect()
}
