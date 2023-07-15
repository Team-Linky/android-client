package com.linky.more_activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.linky.lock.lockScreen
import com.linky.tip.tipScreen

@Composable
internal fun MoreNavHost(
    navHostController: NavHostController,
    startDestination: String,
    onClose: () -> Unit,
    onLinkCreate: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        tipScreen(
            onClose = onClose,
            onLinkCreate = onLinkCreate
        )
        lockScreen(
            onBack = onClose
        )
    }
}