package com.linky.link_input_complete.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.linky.navigation.link.LinkNavType

internal val AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition: ExitTransition
    get() = if (targetState.destination.route == LinkNavType.URLInput.route) {
        AnimatedContentTransitionScope.SlideDirection.Start
    } else {
        AnimatedContentTransitionScope.SlideDirection.End
    }.let { towards ->
        slideOutOfContainer(
            towards = towards,
            animationSpec = tween(300)
        )
    }