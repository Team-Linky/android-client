package com.linky.link

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.linky.link_detail_input.detailInputScreen
import com.linky.link_url_input.urlInputScreen
import com.linky.link_input_complete.inputCompleteInputScreen
import com.linky.navigation.link.LinkNavType

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun LinkNavHost(
    navHostController: NavHostController,
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = LinkNavType.URLInput.route
    ) {
        urlInputScreen(navHostController)
        detailInputScreen(navHostController)
        inputCompleteInputScreen(navHostController)
    }
}