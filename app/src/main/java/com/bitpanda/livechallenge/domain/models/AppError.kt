package com.bitpanda.livechallenge.domain.models

import java.io.IOException

sealed interface AppError {
    data class NetworkError(val exception: IOException) : AppError

    data class BadRequest(val message: String?) : AppError

    data class ServerError(val message: String?) : AppError

    data class Unknown(val message: String?) : AppError

    data object Connectivity : AppError

    data object Server : AppError

    data object NoConnectionError : AppError
}
