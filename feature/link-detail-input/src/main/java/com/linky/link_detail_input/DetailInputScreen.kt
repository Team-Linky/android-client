package com.linky.link_detail_input

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.link_detail_input.animation.exitTransition
import com.linky.link_detail_input.component.DetailInputContent
import com.linky.link_detail_input.component.DetailInputHeader
import com.linky.model.Link
import com.linky.navigation.link.LinkNavType
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

fun NavGraphBuilder.detailInputScreen(navController: NavController) {
    composable(
        route = LinkNavType.DetailInput.route,
        arguments = listOf(
            navArgument("url") { type = NavType.StringType }
        ),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = { exitTransition }
    ) {
        DetailInputRoute(
            onComplete = {
                navController.navigate(route = LinkNavType.Complete.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onBack = navController::popBackStack
        )
    }
}

@Composable
private fun DetailInputRoute(
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    DetailInputScreen(
        onComplete = onComplete,
        onBack = onBack
    )
}

@Composable
private fun DetailInputScreen(
    onComplete: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: DetailInputViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val tags = viewModel.tagsState.collectAsStateWithLifecycle().value
    val focusManager = LocalFocusManager.current
    var memoText by rememberSaveable { mutableStateOf("") }
    val memoFocusRequester = remember { FocusRequester() }

    var tagText by rememberSaveable { mutableStateOf("") }
    val tagFocusRequester = remember { FocusRequester() }
    var selectedTagIds by remember { mutableStateOf(emptyList<Long>()) }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SideEffect.LinkInsertFail -> Log.d("123123", "실패!")
            is SideEffect.LinkInsertSuccess -> onComplete.invoke()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DetailInputHeader(
            isNextActive = true,
            onComplete = {
                if (state is State.Success) {
                    val link = Link(
                        memo = memoText,
                        openGraphData = state.openGraphData,
                        tags = selectedTagIds,
                    )
                    viewModel.addLink(link)
                } else {
                    Log.d("123123", "올바른 url이 아닙니다.")
                }
            },
            onBack = onBack
        )
        DetailInputContent(
            state = state,
            tags = tags,
            selectTags = selectedTagIds,
            memoValue = memoText,
            memoOnValueChange = { memoText = it },
            memoOnClear = { memoText = "" },
            memoFocusRequester = memoFocusRequester,
            tagValue = tagText,
            tagOnValueChange = { tagText = it },
            tagOnClear = { tagText = "" },
            tagFocusRequester = tagFocusRequester,
            focusManager = focusManager,
            onSelectTag = { tag ->
                val newList = selectedTagIds.toMutableList()
                newList.add(tag.id!!)
                selectedTagIds = newList
            },
            onUnSelectTag = { tag ->
                val newList = selectedTagIds.toMutableList()
                newList.remove(tag.id!!)
                selectedTagIds = newList
            },
            onDeleteTag = { tag ->
                viewModel.deleteTag(tag.id!!)
            },
            onCreateTag = { newTag ->
                viewModel.addTag(newTag)
                tagText = ""
            },
        )
    }
}