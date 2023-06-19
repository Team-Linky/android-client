package com.linky.more

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.linky.more.component.MoreContent
import com.linky.more.component.MoreHeader
import com.linky.navigation.NavType

fun NavGraphBuilder.moreScreen() {
    composable(NavType.More.route) { MoreRoute() }
}

@Composable
private fun MoreRoute() {
    MoreScreen()
}

@Composable
private fun MoreScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoreHeader()
        MoreContent()
    }
}