package com.linky.link

import android.app.Activity
import android.content.Intent
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
import com.linky.common.activity_stack_counter.ActivityStackObserver
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
internal fun LinkNavHost(
    navHostController: NavHostController,
    scaffoldState: ScaffoldState,
    activityStackObserver: com.linky.common.activity_stack_counter.ActivityStackObserver,
    onFinishAndResult: (Bundle) -> Unit,
    onBack: () -> Unit,
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = "startDestination",
    ) {
        defaultScreen(
            navController = navHostController,
            activityStackObserver = activityStackObserver,
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
    activityStackObserver: com.linky.common.activity_stack_counter.ActivityStackObserver
) {
    composable("startDestination") {
        val activity = LocalContext.current as Activity
        val intent = activity.intent

        if (intent.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                if (activityStackObserver.count == 1) {
                    val mainIntent = Intent("com.linky.MAIN_ACTIVITY")
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity.startActivity(mainIntent)

                    val linkIntent = Intent(activity, LinkActivity::class.java).apply {
                        action = intent.action
                        type = intent.type
                        putExtra(Intent.EXTRA_TEXT, intent.getStringExtra(Intent.EXTRA_TEXT))
                        putExtras(intent)
                    }
                    activity.startActivity(linkIntent)
                }

                intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedText ->
                    val encodedUrl = URLEncoder.encode(sharedText, StandardCharsets.UTF_8.toString())
                    val route = LinkNavType.LinkModifier.route
                        .replace("{url}", encodedUrl)
                        .replace("{mode}", "1")
                        .replace("{linkId}", "-1")

                    navController.navigate(route = route) {
                        popUpTo("startDestination") { inclusive = true }
                    }
                }
            }
        } else {
            val url = intent.getStringExtra("url")
            val mode = intent.getIntExtra("mode", 0)
            val linkId = intent.getLongExtra("linkId", 0L)

            when (intent.getStringExtra("startDestination")) {
                "link_edit" -> navController.navigatorLinkModifier(url!!, mode, linkId)
                else -> navController.navigate(LinkNavType.URLInput.route)
            }
        }

    }
}