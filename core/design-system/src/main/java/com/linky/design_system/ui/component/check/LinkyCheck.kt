package com.linky.design_system.ui.component.check

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray800
import com.linky.design_system.ui.theme.MainColor

@Composable
fun LinkyCheck(
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedColor: Color = MainColor,
    unCheckedColor: Color = ColorFamilyGray600AndGray800
) {
    val internalModifier = remember(checked) {
        if (checked) {
            Modifier.background(checkedColor, CircleShape)
        } else {
            Modifier.border(1.dp, unCheckedColor, CircleShape)
        }
    }

    Box(
        modifier = internalModifier.then(modifier),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "check",
                tint = Color.White,
            )
        }
    }
}