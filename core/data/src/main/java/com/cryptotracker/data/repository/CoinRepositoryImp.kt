package com.cryptotracker.data.repository

import arrow.core.Either
import com.cryptotracker.data.mappers.AssetsMapper
import com.cryptotracker.data.mappers.RatesMapper
import com.cryptotracker.data.utils.mapResult
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.domain.models.Asset
import com.cryptotracker.domain.models.Rate
import com.cryptotracker.domain.repository.CoinRepository
import com.cryptotracker.network.source.CoinDataSource
import javax.inject.Inject

class CoinRepositoryImp
    @Inject
    constructor(
        private val assetsDataSource: CoinDataSource,
        private val assetsMapper: AssetsMapper,
        private val ratesMapper: RatesMapper,
    ) : CoinRepository {
        override suspend fun getAssets(): Either<AppError, List<Asset>> =
            assetsDataSource.getAssets().mapResult { asset -> assetsMapper.map(input = asset.data) }

        override suspend fun getRates(): Either<AppError, List<Rate>> =
            assetsDataSource.getRates().mapResult { rates -> ratesMapper.map(input = rates.data) }
    }
