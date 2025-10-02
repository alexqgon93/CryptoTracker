package com.bitpanda.livechallenge.ui.coins

interface CoinsContract {
    data class State(
        val screenState: ScreenState = ScreenState.LOADING,
        val isTop: Boolean = true,
        val coins: List<CoinUIModel>? = null
    )

    sealed class UiEvent {

        data object OnTopCoinsClick : UiEvent()

        data object OnBottomCoinsClick : UiEvent()

        data object OnRetryButtonClicked : UiEvent()
    }
}

enum class ScreenState { LOADING, SUCCESS, ERROR }
