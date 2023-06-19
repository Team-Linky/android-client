package com.linky.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.navigation.NavType
import com.linky.timeline.component.TimeLineHeader
import com.linky.timeline.component.TimeLineLinkCreateScreen

fun NavGraphBuilder.timelineScreen(showLinkActivity: () -> Unit) {
    composable(NavType.TimeLine.route) { TimeLineRoute(showLinkActivity) }
}

@Composable
private fun TimeLineRoute(showLinkActivity: () -> Unit) {
    TimeLineScreen(showLinkActivity)
}

@Composable
private fun TimeLineScreen(showLinkActivity: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
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