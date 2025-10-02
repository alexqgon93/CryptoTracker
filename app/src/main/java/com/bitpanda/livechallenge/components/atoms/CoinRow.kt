package com.bitpanda.livechallenge.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.bitpanda.livechallenge.ui.coins.CoinUIModel
import com.bitpanda.livechallenge.ui.theme.Dimens
import com.bitpanda.livechallenge.ui.theme.FontSizes

@Composable
fun CoinRow(uiModel: CoinUIModel) {
    val isPositive = uiModel.changePercent24Hr >= 0.0
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(all = Dimens.dimen_8),
        shape = RoundedCornerShape(size = Dimens.dimen_20),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.dimen_8)
    ) {
        Row(modifier = Modifier.padding(horizontal = Dimens.dimen_24, vertical = Dimens.dimen_20)) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = Dimens.dimen_8),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = Dimens.dimen_8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier =
                        Modifier
                            .background(
                                color = Color(color = 0xFFE5E7EB),
                                shape = RoundedCornerShape(size = Dimens.dimen_16)
                            ).padding(horizontal = Dimens.dimen_16, vertical = Dimens.dimen_8)
                    ) {
                        Text(
                            text = "#${uiModel.rank}",
                            fontSize = FontSizes.font_18,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Text(
                        text = uiModel.symbol,
                        fontSize = FontSizes.font_18,
                        fontWeight = FontWeight.Light
                    )
                }
                Text(
                    text = uiModel.name,
                    fontSize = FontSizes.font_20,
                    fontWeight = FontWeight.Normal
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(space = Dimens.dimen_4)
            ) {
                Text(
                    text = uiModel.price,
                    fontSize = FontSizes.font_20,
                    fontWeight = FontWeight.Normal
                )
                Box(
                    modifier =
                    Modifier
                        .background(
                            color = if (isPositive) {
                                Color(
                                    color = 0xFFD1FAE5
                                )
                            } else {
                                Color(0xFFFEE2E2)
                            },
                            shape = RoundedCornerShape(size = Dimens.dimen_8)
                        ).padding(horizontal = Dimens.dimen_8, vertical = Dimens.dimen_4)
                ) {
                    Text(
                        text = "${uiModel.changePercent24Hr} %",
                        fontSize = FontSizes.font_16,
                        fontWeight = FontWeight.Normal,
                        color = if (isPositive) Color(color = 0xFF047857) else Color(0xFFDC2626)
                    )
                }
                Text(
                    text = "${uiModel.priceDifference24Hr} €",
                    fontSize = FontSizes.font_16,
                    fontWeight = FontWeight.Normal,
                    color = if (isPositive) Color(color = 0xFF047857) else Color(0xFFDC2626)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CoinRowPreview() = CoinRow(
    uiModel =
    CoinUIModel(
        id = "bitcoin",
        name = "Bitcoin",
        symbol = "BTC",
        price = "30000",
        changePercent24Hr = 2.5,
        rank = 1,
        priceDifference24Hr = 25.0
    )
)
