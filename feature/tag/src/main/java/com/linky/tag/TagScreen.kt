package com.linky.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.linky.model.Tag
import com.linky.navigation.MainNavType
import com.linky.tag.animation.enterTransition
import com.linky.tag.animation.exitTransition
import com.linky.tag.component.TagHeader
import com.linky.tag.component.TagEmptyScreen
import com.linky.tag.component.TagListScreen

fun NavGraphBuilder.tagScreen(onShowLinkActivity: () -> Unit) {
    composable(
        route = MainNavType.Tag.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) { TagRoute(onShowLinkActivity) }
}

@Composable
private fun TagRoute(
    onShowLinkActivity: () -> Unit,
    tagViewModel: TagViewModel = hiltViewModel()
) {
    val tagList = tagViewModel.tagsState.collectAsStateWithLifecycle().value

    TagScreen(
        onShowLinkActivity = onShowLinkActivity,
        tagList = tagList
    )
}

@Composable
private fun TagScreen(
    onShowLinkActivity: () -> Unit = {},
    tagList: List<Tag>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TagHeader()
        if (tagList.isNotEmpty()) {
            TagListScreen(
                tagList = tagList,
                onClick = {}
            )
        } else {
            TagEmptyScreen(onShowLinkActivity)
        }
    }
}