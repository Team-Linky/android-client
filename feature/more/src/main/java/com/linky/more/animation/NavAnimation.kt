package com.linky.more.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.linky.navigation.MainNavType

internal val AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition: EnterTransition?
    get() = when (initialState.destination.route) {
        MainNavType.TimeLine.route,
        MainNavType.Tag.route -> AnimatedContentTransitionScope.SlideDirection.Start

        else -> null
    }?.let { towards ->
        slideIntoContainer(
            towards = towards,
            animationSpec = tween(300)
        )
    }

internal val AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition: ExitTransition?
    get() = when (targetState.destination.route) {
        MainNavType.TimeLine.route,
        MainNavType.Tag.route -> AnimatedContentTransitionScope.SlideDirection.End
        else -> null
    }?.let { towards ->
        slideOutOfContainer(
            towards = towards,
            animationSpec = tween(300)
        )
    }