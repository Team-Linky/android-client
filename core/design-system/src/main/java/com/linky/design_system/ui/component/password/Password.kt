package com.linky.design_system.ui.component.password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.ShadowBlue

@Composable
fun Password(valueLength: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Password(valueLength > 0)
        Spacer(modifier = Modifier.padding(start = 16.dp))
        Password(valueLength > 1)
        Spacer(modifier = Modifier.padding(start = 16.dp))
        Password(valueLength > 2)
        Spacer(modifier = Modifier.padding(start = 16.dp))
        Password(valueLength > 3)
    }
}

@Composable
private fun Password(isNotEmpty: Boolean) {
    Spacer(
        modifier = Modifier
            .background(
                color = if (isNotEmpty) ColorFamilyGray900AndGray100 else ShadowBlue,
                shape = CircleShape
            )
            .size(12.dp)
    )
}