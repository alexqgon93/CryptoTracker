package com.bitpanda.livechallenge.data.utils

import com.bitpanda.livechallenge.data.remote.responses.NetworkError
import com.bitpanda.livechallenge.data.utils.Failure.BadRequest
import com.bitpanda.livechallenge.data.utils.Failure.NoConnectionError
import com.bitpanda.livechallenge.data.utils.Failure.Unknown
import com.bitpanda.livechallenge.data.utils.Failure.ServerError
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class FailureFactory {
    companion object {
        private const val ERROR = 400
        private const val SERVER_ERROR = 200
    }

    open fun handleCode(code: Int?, message: String?, errorResponse: NetworkError?): Failure =
        when (code) {
            ERROR -> BadRequest(code = code, message = message)
            SERVER_ERROR -> ServerError(code = code, message = message)
            else -> Unknown(message = "Unknown Error")
        }

    open fun handleException(exception: Exception) = when (exception) {
        is UnknownHostException, is SocketTimeoutException -> NoConnectionError
        else -> NoConnectionError
    }

}