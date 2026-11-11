package com.cryptotracker.network.utils

import com.cryptotracker.network.responses.NetworkError
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class FailureFactory {
    companion object {
        private const val ERROR = 400
        private const val SERVER_ERROR = 200
    }

    open fun handleCode(
        code: Int?,
        message: String?,
        errorResponse: NetworkError?,
    ): Failure =
        when (code) {
            ERROR -> Failure.BadRequest(code = code, message = message)
            SERVER_ERROR -> Failure.ServerError(code = code, message = message)
            else -> Failure.Unknown(message = "Unknown Error")
        }

    open fun handleException(exception: Exception) =
        when (exception) {
            is UnknownHostException, is SocketTimeoutException -> Failure.NoConnectionError
            else -> Failure.NoConnectionError
        }
}
