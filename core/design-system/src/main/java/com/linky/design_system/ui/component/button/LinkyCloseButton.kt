package com.linky.design_system.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.linky.design_system.R
import com.linky.design_system.util.clickableRipple

@Composable
fun LinkyCloseButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(R.drawable.icon_close),
        contentDescription = "close",
        modifier = Modifier.clickableRipple(
            radius = 15.dp,
            onClick = onClick
        )
    )
}