package com.linky.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.navigation.MainNavType
import com.linky.timeline.animation.enterTransition
import com.linky.timeline.animation.exitTransition
import com.linky.timeline.component.TimeLineHeader
import com.linky.timeline.component.TimeLineLinkCreateScreen

fun NavGraphBuilder.timelineScreen(showLinkActivity: () -> Unit) {
    composable(
        route = MainNavType.TimeLine.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) { TimeLineRoute(showLinkActivity) }
}

@Composable
private fun TimeLineRoute(showLinkActivity: () -> Unit) {
    TimeLineScreen(showLinkActivity)
}

@Composable
private fun TimeLineScreen(showLinkActivity: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeLineHeader()
        TimeLineLinkCreateScreen(showLinkActivity)
    }
}

@Preview
@Composable
private fun TimeLinePreview() {
    LinkyDefaultTheme {
        TimeLineScreen()
    }
}