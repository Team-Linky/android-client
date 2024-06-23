package com.linky.android

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.linky.feature.tag_modifier.tagModifierScreen
import com.linky.more.moreScreen
import com.linky.navigation.MainNavType
import com.linky.tag.tagScreen
import com.linky.timeline.timelineScreen

@Composable
fun MainNavHost(
    scaffoldState: ScaffoldState,
    navHostController: NavHostController,
    onShowLinkActivity: () -> Unit,
    onShowMoreActivity: (String) -> Unit,
    onClickTagAdd: (Long) -> Unit,
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = MainNavType.TimeLine.route
    ) {
        timelineScreen(
            scaffoldState = scaffoldState,
            onShowLinkActivity = onShowLinkActivity,
            onClickTagAdd = onClickTagAdd
        )
        tagScreen(
            scaffoldState = scaffoldState,
            onShowLinkActivity = onShowLinkActivity
        )
        moreScreen(
            onShowMoreActivity = onShowMoreActivity
        )
        tagModifierScreen(
            onBack = navHostController::popBackStack
        )
    }
}