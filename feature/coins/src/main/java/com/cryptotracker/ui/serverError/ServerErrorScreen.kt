@file:Suppress("FunctionName")

package com.cryptotracker.ui.serverError

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.cryptotracker.feature.coins.R
import com.cryptotracker.ui.theme.Dimens
import com.cryptotracker.ui.theme.FontSizes

@Composable
fun ServerErrorScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    message: (@Composable () -> Unit)? = null,
) {
    val retryContentDescription = stringResource(id = R.string.retry_button_content_description)
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(all = Dimens.dimen_24)
                .semantics { liveRegion = LiveRegionMode.Polite },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(all = Dimens.dimen_32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = Dimens.dimen_16),
        ) {
            Icon(
                modifier = Modifier.size(size = Dimens.dimen_120),
                imageVector = Icons.Default.CloudOff,
                contentDescription = stringResource(id = R.string.error_icon_content_description),
                tint = Color(color = 0xFFEF4444),
            )
            Spacer(modifier = Modifier.height(height = Dimens.dimen_16))
            Text(
                text = stringResource(R.string.error_message_title),
                fontSize = FontSizes.font_24,
                fontWeight = FontWeight.Normal,
                color = Color(color = 0xFF111827),
            )
            message?.invoke()
            Button(
                modifier = Modifier.semantics { contentDescription = retryContentDescription },
                onClick = onRetry,
            ) {
                Text(text = stringResource(id = R.string.retry_button_title))
            }
        }
    }
}

@Preview
@Composable
fun ServerErrorScreenPreview() = ServerErrorScreen(onRetry = {})
