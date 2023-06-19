package com.linky.link_detail_input

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.link_detail_input.component.DetailInputContent
import com.linky.link_detail_input.component.DetailInputHeader
import com.linky.navigation.link.LinkNavType

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.detailInputScreen(navController: NavController) {
    composable(
        route = LinkNavType.DetailInput.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
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
    onBack: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var memoText by rememberSaveable { mutableStateOf("") }
    val memoFocusRequester = remember { FocusRequester() }

    var tagText by rememberSaveable { mutableStateOf("") }
    val tagFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DetailInputHeader(
            isNextActive = true,
            onComplete = onComplete,
            onBack = onBack
        )
        DetailInputContent(
            memoValue = memoText,
            memoOnValueChange = { memoText = it },
            memoOnClear = { memoText = "" },
            memoFocusRequester = memoFocusRequester,
            tagValue = tagText,
            tagOnValueChange = { tagText = it },
            tagOnClear = { tagText = "" },
            tagFocusRequester = tagFocusRequester,
            focusManager = focusManager,
        )
    }
}