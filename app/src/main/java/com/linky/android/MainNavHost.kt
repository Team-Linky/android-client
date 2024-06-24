package com.linky.android

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.linky.model.Link
import com.linky.more.moreScreen
import com.linky.navigation.MainNavType
import com.linky.tag.tagScreen
import com.linky.timeline.timelineScreen

@Composable
fun MainNavHost(
    scaffoldState: ScaffoldState,
    navHostController: NavHostController,
    onShowLinkActivity: () -> Unit,
    onShowTimeLineActivity: (String) -> Unit,
    onShowMoreActivity: (String) -> Unit,
    onEdit: (Link) -> Unit,
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = MainNavType.TimeLine.route
    ) {
        timelineScreen(
            scaffoldState = scaffoldState,
            onShowLinkActivity = onShowLinkActivity,
            onEdit = onEdit
        )
        tagScreen(
            onShowLinkActivity = onShowLinkActivity,
            onShowTimeLineActivity = onShowTimeLineActivity,
        )
        moreScreen(
            onShowMoreActivity = onShowMoreActivity
        )
    }
}