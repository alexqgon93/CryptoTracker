package com.cryptotracker.network.utils

import arrow.core.Either
import arrow.core.left
import com.cryptotracker.network.responses.NetworkError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T> tryCall(call: suspend () -> Response<T>): Either<Failure, T> =
    try {
        call().safeCall()
    } catch (ex: Exception) {
        ex.toError().left()
    }

fun <T> Response<T>.safeCall(errorFactory: FailureFactory = FailureFactory()): Either<Failure, T> =
    try {
        val response = this
        val baseResponse = response.body()
        val errorBody = response.getErrorBody()
        when (response.isSuccessful && baseResponse != null) {
            true -> Either.Right(baseResponse)
            else ->
                Either.Left(
                    errorFactory.handleCode(
                        code = response.code(),
                        message = response.message(),
                        errorResponse = errorBody,
                    ),
                )
        }
    } catch (exception: Exception) {
        Either.Left(errorFactory.handleException(exception))
    }

fun <T> Response<T>.getErrorBody(): NetworkError? {
    val errorBody = errorBody()?.string()
    if (errorBody != null) {
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<NetworkError> = moshi.adapter(NetworkError::class.java)
        return adapter.fromJson(errorBody)
    }
    return null
}

fun Throwable.toError(): Failure =
    when (this) {
        is IOException -> Failure.Connectivity
        is HttpException -> Failure.Server(code = code())
        else -> Failure.Unknown(message = message)
    }
