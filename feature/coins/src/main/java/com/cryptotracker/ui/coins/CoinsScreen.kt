@file:Suppress("FunctionName")

package com.cryptotracker.ui.coins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.cryptotracker.components.atoms.CoinRow
import com.cryptotracker.components.atoms.ProgressIndicator
import com.cryptotracker.components.molecules.CoinsToggleRow
import com.cryptotracker.components.molecules.CoinsTopBar
import com.cryptotracker.feature.coins.R
import com.cryptotracker.ui.coins.CoinsContract.State
import com.cryptotracker.ui.coins.CoinsContract.UiEvent
import com.cryptotracker.ui.serverError.ServerErrorScreen
import com.cryptotracker.ui.theme.Dimens

@Composable
fun CoinsScreen(viewModel: CoinsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    CoinScreenContent(state = state, onPushEvent = viewModel::onUiEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinScreenContent(
    state: State,
    onPushEvent: (UiEvent) -> Unit,
) = when (state.screenState) {
    ScreenState.LOADING -> ProgressIndicator()
    ScreenState.SUCCESS -> {
        val coins = state.coins.orEmpty()

        Scaffold(topBar = {
            CoinsTopBar(title = stringResource(id = R.string.cryptocurrency_prices_title))
        }) { paddingValues ->
            PullToRefreshBox(
                modifier = Modifier.padding(paddingValues),
                isRefreshing = state.isRefreshing,
                onRefresh = { onPushEvent(UiEvent.OnPullToRefresh) },
            ) {
                Column(
                    modifier =
                        Modifier
                            .padding(top = Dimens.dimen_20)
                            .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.dimen_8),
                ) {
                    CoinsToggleRow(isTop = state.isTop, onToggle = onPushEvent)
                    HorizontalDivider(thickness = Dimens.dimen_1)
                    state.errorMessage?.let { errorMessage ->
                        Text(
                            modifier =
                                Modifier.padding(
                                    start = Dimens.dimen_24,
                                    end = Dimens.dimen_24,
                                ),
                            text = errorMessage,
                            color = Color(color = 0xFFB91C1C),
                        )
                    }
                    if (coins.isEmpty()) {
                        EmptyCoinsState(modifier = Modifier.weight(weight = 1f))
                    } else {
                        val listContentDescription = stringResource(id = R.string.coins_list_content_description)
                        LazyVerticalGrid(
                            modifier =
                                Modifier
                                    .weight(weight = 1f)
                                    .fillMaxWidth()
                                    .padding(start = Dimens.dimen_16, end = Dimens.dimen_16)
                                    .semantics { contentDescription = listContentDescription },
                            columns = GridCells.Fixed(count = 1),
                            horizontalArrangement = Arrangement.spacedBy(space = Dimens.dimen_4),
                        ) {
                            items(items = coins, key = { coin -> coin.id }) { coin ->
                                CoinRow(uiModel = coin)
                            }
                        }
                    }
                }
            }
        }
    }

    ScreenState.ERROR ->
        ServerErrorScreen(onRetry = { onPushEvent(UiEvent.OnRetryButtonClicked) }) {
            Text(text = state.errorMessage ?: stringResource(id = R.string.error_message_body))
        }
}

@Composable
private fun EmptyCoinsState(modifier: Modifier = Modifier) =
    Box(
        modifier = modifier.padding(all = Dimens.dimen_24),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = Dimens.dimen_8),
        ) {
            Text(
                text = stringResource(id = R.string.empty_coins_title),
                fontWeight = FontWeight.Normal,
            )
            Text(text = stringResource(id = R.string.empty_coins_body))
        }
    }
