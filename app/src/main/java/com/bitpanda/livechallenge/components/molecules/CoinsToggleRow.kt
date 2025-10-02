package com.bitpanda.livechallenge.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bitpanda.livechallenge.R
import com.bitpanda.livechallenge.components.atoms.OutlinedToggleButton
import com.bitpanda.livechallenge.ui.coins.CoinsContract.UiEvent
import com.bitpanda.livechallenge.ui.theme.Dimens

@Composable
fun CoinsToggleRow(modifier: Modifier = Modifier, isTop: Boolean, onToggle: (UiEvent) -> Unit) =
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(start = Dimens.dimen_16, end = Dimens.dimen_16),
        horizontalArrangement = Arrangement.spacedBy(space = Dimens.dimen_8)
    ) {
        OutlinedToggleButton(
            modifier = modifier.weight(weight = 1f),
            selected = isTop,
            onClick = { onToggle(UiEvent.OnTopCoinsClick) },
            icon = Filled.TrendingUp,
            text = stringResource(id = R.string.trending_top_title),
            contentDescription = stringResource(id = R.string.trending_top_title)
        )
        OutlinedToggleButton(
            modifier = modifier.weight(weight = 1f),
            selected = !isTop,
            onClick = { onToggle(UiEvent.OnBottomCoinsClick) },
            icon = Filled.TrendingDown,
            text = stringResource(id = R.string.trending_lowest_title),
            contentDescription = stringResource(id = R.string.trending_lowest_title)
        )
    }

@Preview
@Composable
private fun CoinsToggleRowPreview() = CoinsToggleRow(isTop = true, onToggle = {})
