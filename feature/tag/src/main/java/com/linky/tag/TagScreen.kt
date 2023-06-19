package com.linky.tag

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.linky.navigation.NavType
import com.linky.tag.component.TagHeader
import com.linky.tag.component.TagLinkCreateScreen

fun NavGraphBuilder.tagScreen(showLinkActivity: () -> Unit) {
    composable(NavType.Tag.route) { TagRoute(showLinkActivity) }
}

@Composable
private fun TagRoute(showLinkActivity: () -> Unit) {
    TagScreen(showLinkActivity)
}

@Composable
private fun TagScreen(showLinkActivity: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TagHeader()
        TagLinkCreateScreen(showLinkActivity)
    }
}