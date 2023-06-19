package com.linky.android

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.linky.more.moreScreen
import com.linky.navigation.NavType
import com.linky.tag.tagScreen
import com.linky.timeline.timelineScreen

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    showLinkActivity: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = NavType.TimeLine.route
    ) {
        timelineScreen(showLinkActivity)
        tagScreen(showLinkActivity)
        moreScreen()
    }
}