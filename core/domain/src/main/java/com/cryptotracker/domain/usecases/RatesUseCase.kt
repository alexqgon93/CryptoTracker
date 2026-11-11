package com.cryptotracker.domain.usecases

import arrow.core.Either
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.domain.models.Rate
import com.cryptotracker.domain.repository.CoinRepository
import javax.inject.Inject

class RatesUseCase
    @Inject
    constructor(
        private val repository: CoinRepository,
    ) {
        suspend operator fun invoke(): Either<AppError, List<Rate>> = repository.getRates()
    }
