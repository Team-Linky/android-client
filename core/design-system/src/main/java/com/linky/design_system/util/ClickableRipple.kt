package com.linky.design_system.util

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.clickableRipple(
    bounded: Boolean = true,
    enableRipple: Boolean = true,
    radius: Dp = Dp.Unspecified,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = if (enableRipple) {
        rememberRipple(
            bounded = bounded,
            radius = radius
        )
    } else {
        null
    }
    clickable(
        interactionSource = interactionSource,
        indication = indication,
        onClick = onClick
    )
}