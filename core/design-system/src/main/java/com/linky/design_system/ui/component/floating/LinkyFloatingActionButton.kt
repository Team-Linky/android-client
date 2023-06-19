package com.linky.design_system.ui.component.floating

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.linky.design_system.R
import com.linky.design_system.util.clickableRipple

@Composable
fun LinkyFloatingActionButton(onClick: () -> Unit = {}) {
    Image(
        painter = painterResource(R.drawable.image_floating),
        contentDescription = "add link",
        modifier = Modifier
            .padding(top = 40.dp, end = 10.dp)
            .clickableRipple(
                radius = 24.dp,
                onClick = onClick
            )
    )
}

@Composable
@Preview
private fun LinkyFloatingActionButtonPreview() {
    LinkyFloatingActionButton()
}