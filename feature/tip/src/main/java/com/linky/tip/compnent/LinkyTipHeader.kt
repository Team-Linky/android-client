package com.linky.tip.compnent

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linky.design_system.ui.component.button.LinkyBackArrowWhiteButton
import com.linky.design_system.ui.component.header.LinkyHeader

@Composable
internal fun LinkyTipHeader(onClose: () -> Unit) {
    LinkyHeader(modifier = Modifier.padding(start = 12.dp)) {
        LinkyBackArrowWhiteButton(onClick = onClose)
    }
}