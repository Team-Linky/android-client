package com.linky.design_system.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColors(
    primary = MainColorDark,
    secondary = MainColorDark,
    background = Gray900,
    surface = Gray900
)

private val LightColorScheme = lightColors(
    primary = MainColorLight,
    secondary = MainColorDark,
    background = Gray100,
    surface = Gray100
)

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Unspecified
    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0f, 0f, 0f, 0f)
}

@Composable
fun LinkyDefaultTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val systemUiController = rememberSystemUiController()
    val navigationColor = LinkyDefaultBackgroundColor

    SideEffect {
        systemUiController.setStatusBarColor(colors.background)
        systemUiController.setNavigationBarColor(navigationColor)
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}

@Composable
fun LinkyLinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorScheme.copy(background = Gray999, surface = Gray999)
    } else {
        LightColorScheme.copy(background = White, surface = White)
    }

    val systemUiController = rememberSystemUiController()
    val navigationColor = LinkyDefaultBackgroundColor

    SideEffect {
        systemUiController.setStatusBarColor(colors.background)
        systemUiController.setNavigationBarColor(navigationColor)
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}