package com.linky.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.linky.more.animation.enterTransition
import com.linky.more.animation.exitTransition
import com.linky.more.component.MoreContent
import com.linky.more.component.MoreHeader
import com.linky.navigation.MainNavType

fun NavGraphBuilder.moreScreen(
    onShowMoreActivity: (String) -> Unit,
    onShowTagSettingActivity: () -> Unit,
) {
    composable(
        route = MainNavType.More.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) {
        MoreRoute(
            onShowMoreActivity = onShowMoreActivity,
            onShowTagSettingActivity = onShowTagSettingActivity,
        )
    }
}

@Composable
private fun MoreRoute(
    onShowMoreActivity: (String) -> Unit,
    onShowTagSettingActivity: () -> Unit,
) {
    MoreScreen(
        onShowMoreActivity = onShowMoreActivity,
        onShowTagSettingActivity = onShowTagSettingActivity,
    )
}

@Composable
private fun MoreScreen(
    onShowMoreActivity: (String) -> Unit = {},
    onShowTagSettingActivity: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoreHeader()
        MoreContent(
            onShowMoreActivity = onShowMoreActivity,
            onShowTagSettingActivity = onShowTagSettingActivity,
        )
    }
}