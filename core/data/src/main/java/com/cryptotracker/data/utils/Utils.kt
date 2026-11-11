package com.cryptotracker.data.utils

import arrow.core.Either
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.network.utils.Failure

fun <T, R> Either<Failure, T>.mapResult(successTransform: (T) -> R): Either<AppError, R> =
    fold(
        ifLeft = { error -> Either.Left(value = error.toAppError()) },
        ifRight = { success -> Either.Right(value = successTransform(success)) },
    )

fun Failure.toAppError(): AppError =
    when (this) {
        is Failure.BadRequest -> AppError.BadRequest(message = message)
        is Failure.ServerError -> AppError.ServerError(message = message)
        is Failure.Unknown -> AppError.Unknown(message = message)
        Failure.NoConnectionError -> AppError.NoConnectionError
        Failure.Connectivity -> AppError.Connectivity
        is Failure.Server -> AppError.Server
        is Failure.NetworkError -> AppError.NetworkError(exception = this.exception)
    }
