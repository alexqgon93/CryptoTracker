package com.bitpanda.livechallenge.ui.coins

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitpanda.livechallenge.domain.usecases.AssetsUseCase
import com.bitpanda.livechallenge.domain.usecases.RatesUseCase
import com.bitpanda.livechallenge.ui.coins.CoinsContract.State
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnBottomCoinsClick
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnPullToRefresh
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnRetryButtonClicked
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent.OnTopCoinsClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val assetsUseCase: AssetsUseCase,
    private val ratesUseCase: RatesUseCase,
    private val coinUiMapper: CoinUiMapper,
) : ViewModel() {

    private lateinit var coins: List<CoinUIModel>
    var state by mutableStateOf(value = State())
        private set

    init {
        load()
    }

    fun onUiEvent(uiEvent: UiEvent) = when (uiEvent) {
        OnTopCoinsClick -> state = state.copy(isTop = true, coins = getCoins())
        OnBottomCoinsClick -> state = state.copy(isTop = false, coins = getCoins(false))
        OnRetryButtonClicked -> {
            state = state.copy(screenState = ScreenState.LOADING, coins = emptyList())
            load()
        }

        OnPullToRefresh -> {}
    }

    private fun load() = viewModelScope.launch(context = Dispatchers.IO) {
        val assets = async { assetsUseCase() }.await().getOrNull()
        val rates = async { ratesUseCase() }.await().getOrNull()

        if (assets != null && rates != null) {
            val eurRate = rates.find { it.id == "euro" }?.rateUsd?.toDoubleOrNull() ?: 1.0
            val coinsInEur = assets.map { coins -> coins.copy(price = coins.price * eurRate) }
            coins = coinUiMapper.map(coins = coinsInEur)
            state = state.copy(screenState = ScreenState.SUCCESS, coins = getCoins())
        } else {
            state = state.copy(screenState = ScreenState.ERROR)
        }
    }

    private fun getCoins(top: Boolean = true) =
        if (top) coins.sortedBy { it.rank }.take(n = 10) else coins.sortedByDescending { it.rank }
            .take(n = 10)
}