package com.bitpanda.livechallenge.domain.usecases

import com.bitpanda.livechallenge.domain.repository.CoinRepository
import javax.inject.Inject

class DisconnectAssetsUseCase
@Inject
constructor(private val repository: CoinRepository) {
    suspend operator fun invoke() = repository.disconnect()
}
