package com.bitpanda.livechallenge.domain.usecases

import arrow.core.Either
import com.bitpanda.livechallenge.domain.models.AppError
import com.bitpanda.livechallenge.domain.models.Rate
import com.bitpanda.livechallenge.domain.repository.CoinRepository
import javax.inject.Inject

class RatesUseCase @Inject constructor(private val repository: CoinRepository) {
    suspend operator fun invoke(): Either<AppError, List<Rate>> = repository.getRates()
}