package com.bitpanda.livechallenge.components.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bitpanda.livechallenge.R
import com.bitpanda.livechallenge.ui.theme.Dimens

@Composable
fun OutlinedToggleButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    contentDescription: String
) = OutlinedButton(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(size = Dimens.dimen_12),
    onClick = onClick,
    colors = ButtonDefaults.outlinedButtonColors(
        containerColor = if (selected) Color.Black else Color.Transparent,
        contentColor = if (selected) Color.White else Color.Black,
    ),
    border = ButtonDefaults.outlinedButtonBorder.copy(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Black,
                Color.Black
            )
        )
    )
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
        Text(text = text)
    }
}

@Preview
@Composable
fun OutlinedToggleButtonPreview() = OutlinedToggleButton(
    selected = true,
    onClick = {},
    icon = Filled.TrendingUp,
    text = stringResource(id = R.string.trending_top_title),
    contentDescription = stringResource(id = R.string.trending_top_title),
)
