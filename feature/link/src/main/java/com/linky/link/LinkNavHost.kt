package com.linky.link

import android.app.Activity
import android.os.Bundle
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.linky.feature.link_modifier.linkModifierScreen
import com.linky.feature.link_modifier.navigatorLinkModifier
import com.linky.link_input_complete.inputCompleteInputScreen
import com.linky.link_url_input.urlInputScreen
import com.linky.navigation.link.LinkNavType

@Composable
internal fun LinkNavHost(
    navHostController: NavHostController,
    scaffoldState: ScaffoldState,
    onFinishAndResult: (Bundle) -> Unit,
    onBack: () -> Unit,
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = "startDestination",
    ) {
        defaultScreen(
            navController = navHostController,
        )
        urlInputScreen(
            navController = navHostController
        )
        linkModifierScreen(
            scaffoldState = scaffoldState,
            onCompleteCreate = {
                navHostController.navigate(route = LinkNavType.Complete.route) {
                    popUpTo(navHostController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onCompleteEdit = { message ->
                val data = bundleOf(
                    "cmd" to "showSnackBar",
                    "msg" to message,
                )
                onFinishAndResult.invoke(data)
            },
            onBack = onBack
        )
        inputCompleteInputScreen(
            navController = navHostController
        )
    }
}

private fun NavGraphBuilder.defaultScreen(
    navController: NavController,
) {
    composable("startDestination") {
        val activity = LocalContext.current as Activity
        val intent = activity.intent

        val url = intent.getStringExtra("url")
        val mode = intent.getIntExtra("mode", 0)
        val linkId = intent.getLongExtra("linkId", 0L)

        when (intent.getStringExtra("startDestination")) {
            "link_edit" -> navController.navigatorLinkModifier(url!!, mode, linkId)
            else -> navController.navigate(LinkNavType.URLInput.route)
        }
    }
}