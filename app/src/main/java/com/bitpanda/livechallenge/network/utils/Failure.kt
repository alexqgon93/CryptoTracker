package com.bitpanda.livechallenge.network.utils

import java.io.IOException

sealed interface Failure {
    data class NetworkError(val exception: IOException) : Failure

    data class BadRequest(val code: Int?, val message: String?) : Failure

    data class ServerError(val code: Int?, val message: String?) : Failure

    data object Connectivity : Failure

    data class Server(val code: Int) : Failure

    data class Unknown(val message: String?) : Failure

    data object NoConnectionError : Failure
}
