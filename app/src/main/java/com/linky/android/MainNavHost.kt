package com.linky.android

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.linky.more.moreScreen
import com.linky.navigation.MainNavType
import com.linky.tag.tagScreen
import com.linky.timeline.timelineScreen

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    onShowLinkActivity: () -> Unit,
    onShowMoreActivity: (String) -> Unit
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = MainNavType.TimeLine.route
    ) {
        timelineScreen(onShowLinkActivity)
        tagScreen(onShowLinkActivity)
        moreScreen(onShowMoreActivity)
    }
}