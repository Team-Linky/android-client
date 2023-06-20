package com.linky.link_input_complete

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.linky.link_input_complete.animation.exitTransition
import com.linky.link_input_complete.component.InputCompleteContent
import com.linky.link_input_complete.component.InputCompleteHeader
import com.linky.navigation.link.LinkNavType

fun NavGraphBuilder.inputCompleteInputScreen(navController: NavController) {
    composable(
        route = LinkNavType.Complete.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = { exitTransition }
    ) {
        val activity = LocalContext.current as ComponentActivity

        InputCompleteScreen(
            onClose = activity::finish,
            onCreate = {
                navController.navigate(route = LinkNavType.URLInput.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
private fun InputCompleteScreen(
    onClose: () -> Unit,
    onCreate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputCompleteHeader(onClose)
        InputCompleteContent(onCreate)
    }
}