package com.cryptotracker.domain.usecases

import arrow.core.Either
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.domain.models.Asset
import com.cryptotracker.domain.repository.CoinRepository
import javax.inject.Inject

class AssetsUseCase
    @Inject
    constructor(
        private val repository: CoinRepository,
    ) {
        suspend operator fun invoke(): Either<AppError, List<Asset>> = repository.getAssets()
    }
