package com.linky.tip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.linky.design_system.ui.component.button.LinkyButton
import com.linky.design_system.ui.theme.ColorFamilyGray100AndNav900
import com.linky.navigation.more.MoreNavType
import com.linky.tip.compnent.LinkyTipHeader

fun NavGraphBuilder.tipScreen(
    onClose: () -> Unit,
    onLinkCreate: () -> Unit
) {
    composable(MoreNavType.Tip.route) { LinkyTipRoute(onClose, onLinkCreate) }
}

@Composable
private fun LinkyTipRoute(
    onClose: () -> Unit = {},
    onLinkCreate: () -> Unit = {}
) {
    LinkyTipScreen(
        onClose = onClose,
        onLinkCreate = onLinkCreate
    )
}

@Composable
private fun LinkyTipScreen(
    onClose: () -> Unit = {},
    onLinkCreate: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFamilyGray100AndNav900)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = R.drawable.image_linky_tip,
                        contentDescription = "linky tip",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        filterQuality = FilterQuality.High
                    )
                    LinkyTipHeader(onClose)
                }
            }
            item {
                LinkyButton(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 30.dp)
                        .fillMaxWidth()
                        .height(46.dp),
                    text = stringResource(R.string.link_direct_create_text),
                    onClick = onLinkCreate
                )
            }
        }
    }
}