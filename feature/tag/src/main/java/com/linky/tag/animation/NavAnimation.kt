package com.linky.tag.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.linky.navigation.MainNavType

internal val AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition: EnterTransition?
    get() = when (initialState.destination.route) {
        MainNavType.TimeLine.route -> AnimatedContentTransitionScope.SlideDirection.Start
        MainNavType.More.route -> AnimatedContentTransitionScope.SlideDirection.End
        else -> null
    }?.let { towards ->
        slideIntoContainer(
            towards = towards,
            animationSpec = tween(300)
        )
    }

internal val AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition: ExitTransition?
    get() = when (targetState.destination.route) {
        MainNavType.TimeLine.route -> AnimatedContentTransitionScope.SlideDirection.End
        MainNavType.More.route -> AnimatedContentTransitionScope.SlideDirection.Start
        else -> null
    }?.let { towards ->
        slideOutOfContainer(
            towards = towards,
            animationSpec = tween(300)
        )
    }