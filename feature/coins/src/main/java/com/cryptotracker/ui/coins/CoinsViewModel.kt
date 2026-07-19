package com.cryptotracker.ui.coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.cryptotracker.domain.models.AppError
import com.cryptotracker.domain.models.Asset
import com.cryptotracker.domain.models.Rate
import com.cryptotracker.domain.usecases.AssetsUseCase
import com.cryptotracker.domain.usecases.RatesUseCase
import com.cryptotracker.ui.coins.CoinsContract.State
import com.cryptotracker.ui.coins.CoinsContract.UiEvent
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnBottomCoinsClick
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnPullToRefresh
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnRetryButtonClicked
import com.cryptotracker.ui.coins.CoinsContract.UiEvent.OnTopCoinsClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel : ViewModel {
    private val assetsUseCase: AssetsUseCase
    private val ratesUseCase: RatesUseCase
    private val coinUiMapper: CoinUiMapper
    private val ioDispatcher: CoroutineDispatcher

    @Inject
    constructor(
        assetsUseCase: AssetsUseCase,
        ratesUseCase: RatesUseCase,
        coinUiMapper: CoinUiMapper,
    ) : this(
        assetsUseCase = assetsUseCase,
        ratesUseCase = ratesUseCase,
        coinUiMapper = coinUiMapper,
        ioDispatcher = Dispatchers.IO,
    )

    internal constructor(
        assetsUseCase: AssetsUseCase,
        ratesUseCase: RatesUseCase,
        coinUiMapper: CoinUiMapper,
        ioDispatcher: CoroutineDispatcher,
    ) : super() {
        this.assetsUseCase = assetsUseCase
        this.ratesUseCase = ratesUseCase
        this.coinUiMapper = coinUiMapper
        this.ioDispatcher = ioDispatcher
        load()
    }

    private var cachedCoins: List<CoinUIModel> = emptyList()
    private val _state = MutableStateFlow(value = State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun onUiEvent(uiEvent: UiEvent) =
        when (uiEvent) {
            OnTopCoinsClick, OnBottomCoinsClick -> {
                val isTop = uiEvent == OnTopCoinsClick
                _state.update { currentState ->
                    currentState.copy(isTop = isTop, coins = getCoins(isTop), errorMessage = null)
                }
            }

            OnRetryButtonClicked -> setLoadingState(isRefreshing = false)
            OnPullToRefresh -> {
                if (!_state.value.isRefreshing) {
                    setLoadingState(isRefreshing = true)
                }
                Unit
            }
        }

    private fun setLoadingState(isRefreshing: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                screenState =
                    if (isRefreshing && currentState.screenState == ScreenState.SUCCESS) {
                        currentState.screenState
                    } else {
                        ScreenState.LOADING
                    },
                isRefreshing = isRefreshing,
                errorMessage = null,
            )
        }
        load()
    }

    private fun load() =
        viewModelScope.launch(context = ioDispatcher) {
            val assetsDeferred = async { assetsUseCase() }
            val ratesDeferred = async { ratesUseCase() }
            val assetsResult = assetsDeferred.await()
            val ratesResult = ratesDeferred.await()
            val assets = assetsResult.getOrNull()
            val rates = ratesResult.getOrNull()

            if (assets != null && rates != null) {
                val coinsInEur = mapCoinsToEur(assets, rates)
                updateStateSuccess(coinsInEur)
            } else {
                updateStateError(error = assetsResult.errorOrNull() ?: ratesResult.errorOrNull())
            }
        }

    private fun mapCoinsToEur(
        assets: List<Asset>,
        rates: List<Rate>,
    ): List<Asset> {
        val eurRate = rates.find { it.id == "euro" }?.rateUsd?.toDoubleOrNull() ?: 1.0
        return assets.map { it.copy(price = it.price * eurRate) }
    }

    private fun updateStateSuccess(coinsInEur: List<Asset>) {
        cachedCoins = coinUiMapper.map(coins = coinsInEur)
        _state.update { currentState ->
            currentState.copy(
                screenState = ScreenState.SUCCESS,
                coins = getCoins(currentState.isTop),
                isRefreshing = false,
                errorMessage = null,
            )
        }
    }

    private fun updateStateError(error: AppError?) {
        val errorMessage = error.toUiMessage()
        _state.update { currentState ->
            if (currentState.isRefreshing && cachedCoins.isNotEmpty()) {
                currentState.copy(
                    screenState = ScreenState.SUCCESS,
                    coins = getCoins(currentState.isTop),
                    isRefreshing = false,
                    errorMessage = errorMessage,
                )
            } else {
                currentState.copy(
                    screenState = ScreenState.ERROR,
                    isRefreshing = false,
                    errorMessage = errorMessage,
                )
            }
        }
    }

    private fun getCoins(top: Boolean = true) =
        cachedCoins
            .sortedWith(
                comparator =
                    if (top) {
                        compareByDescending { it.changePercent24Hr }
                    } else {
                        compareBy { it.changePercent24Hr }
                    },
            ).take(10)

    private fun <T> Either<AppError, T>.errorOrNull(): AppError? =
        when (this) {
            is Either.Left -> value
            is Either.Right -> null
        }

    private fun AppError?.toUiMessage(): String =
        when (this) {
            is AppError.BadRequest -> message.takeUnless { it.isNullOrBlank() } ?: GENERIC_REQUEST_ERROR
            AppError.Connectivity,
            AppError.NoConnectionError,
            -> CONNECTION_ERROR
            is AppError.NetworkError -> exception.localizedMessage.takeUnless { it.isNullOrBlank() } ?: CONNECTION_ERROR
            AppError.Server -> SERVER_ERROR
            is AppError.ServerError -> message.takeUnless { it.isNullOrBlank() } ?: SERVER_ERROR
            is AppError.Unknown -> message.takeUnless { it.isNullOrBlank() } ?: UNKNOWN_ERROR
            null -> UNKNOWN_ERROR
        }

    private companion object {
        const val CONNECTION_ERROR = "No internet connection. Check your connection and try again."
        const val GENERIC_REQUEST_ERROR = "We could not complete the price request. Please try again."
        const val SERVER_ERROR = "The price service is not responding. Please try again later."
        const val UNKNOWN_ERROR = "We could not load the latest prices. Please try again."
    }
}
