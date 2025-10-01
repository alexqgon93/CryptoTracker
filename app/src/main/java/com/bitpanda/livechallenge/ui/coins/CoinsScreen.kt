package com.bitpanda.livechallenge.ui.coins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bitpanda.livechallenge.R
import com.bitpanda.livechallenge.components.atoms.CoinRow
import com.bitpanda.livechallenge.components.atoms.ProgressIndicator
import com.bitpanda.livechallenge.components.molecules.CoinsToggleRow
import com.bitpanda.livechallenge.components.molecules.CoinsTopBar
import com.bitpanda.livechallenge.ui.serverError.ServerErrorScreen
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent
import com.bitpanda.livechallenge.ui.coins.CoinsContract.State
import com.bitpanda.livechallenge.ui.theme.Dimens

@Composable
fun CoinsScreen(viewModel: CoinsViewModel = hiltViewModel()) =
    CoinScreenContent(state = viewModel.state, onPushEvent = viewModel::onUiEvent)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinScreenContent(state: State, onPushEvent: (UiEvent) -> Unit) = when (state.screenState) {
    ScreenState.LOADING -> ProgressIndicator()
    ScreenState.SUCCESS -> state.coins?.let { coins ->
        Scaffold(topBar = {
            CoinsTopBar(title = stringResource(id = R.string.cryptocurrency_prices_title))
        }) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(top = Dimens.dimen_20)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.dimen_8)
            ) {
                CoinsToggleRow(isTop = state.isTop, onToggle = onPushEvent)
                HorizontalDivider(thickness = Dimens.dimen_1)
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = Dimens.dimen_16, end = Dimens.dimen_16),
                    columns = GridCells.Fixed(count = 1),
                    horizontalArrangement = Arrangement.spacedBy(space = Dimens.dimen_4),
                ) {
                    items(items = coins) { coin -> CoinRow(uiModel = coin) }
                }
            }
        }
    }

    ScreenState.ERROR -> ServerErrorScreen(onRetry = { onPushEvent(UiEvent.OnRetryButtonClicked) })
}
