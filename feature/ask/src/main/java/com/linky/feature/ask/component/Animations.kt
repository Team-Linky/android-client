package com.linky.feature.ask.component
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

internal val AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition: EnterTransition
    get() = slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(300)
    )

internal val AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition: ExitTransition
    get() = slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(300)
    )