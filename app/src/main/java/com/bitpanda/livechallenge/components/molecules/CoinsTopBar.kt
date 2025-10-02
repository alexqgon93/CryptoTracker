package com.bitpanda.livechallenge.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bitpanda.livechallenge.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinsTopBar(title: String) = TopAppBar(
    title = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = title)
        }
    },
    colors =
    TopAppBarDefaults.topAppBarColors(
        containerColor = Color.LightGray,
        titleContentColor = Color.Black
    )
)

@Preview
@Composable
fun CoinsTopBarPreview() =
    CoinsTopBar(title = stringResource(id = R.string.cryptocurrency_prices_title))
