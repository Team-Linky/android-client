package com.linky.design_system.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class ColorFamily constructor(
    private val lightColor: Color,
    private val darkColor: Color
) {
    val color: Color @Composable get() = if (isSystemInDarkTheme()) darkColor else lightColor
}