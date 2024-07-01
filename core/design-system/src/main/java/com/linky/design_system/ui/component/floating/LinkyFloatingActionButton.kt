package com.linky.design_system.ui.component.floating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray999
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.util.clickableRipple

@Composable
fun LinkyFloatingActionButton(onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .padding(top = 40.dp, end = 10.dp)
            .size(68.dp)
            .background(ColorFamilyWhiteAndGray999, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(MainColor, CircleShape)
                .clickableRipple(radius = 12.dp, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                tint = Color.White,
                contentDescription = "추가"
            )
        }
    }
}

@Composable
@Preview
private fun LinkyFloatingActionButtonPreview() {
    LinkyFloatingActionButton()
}