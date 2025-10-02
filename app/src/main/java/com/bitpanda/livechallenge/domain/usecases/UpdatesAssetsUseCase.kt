package com.bitpanda.livechallenge.domain.usecases

import com.bitpanda.livechallenge.domain.models.Asset
import com.bitpanda.livechallenge.domain.repository.CoinRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UpdatesAssetsUseCase
@Inject
constructor(private val repository: CoinRepository) {
    suspend operator fun invoke(): Flow<List<Asset>> = repository.getUpdates()
}
