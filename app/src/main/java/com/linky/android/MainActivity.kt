package com.linky.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.linky.design_system.ui.component.floating.LinkyFloatingActionButton
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.link.extension.launchLinkActivity
import com.linky.more_activity.extension.launchMoreActivity
import com.linky.navigation.LinkyBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberAnimatedNavController()
            LinkyDefaultTheme {
                Scaffold(
                    bottomBar = { LinkyBottomNavigation(navHostController) },
                    floatingActionButton = { LinkyFloatingActionButton(::launchLinkActivity) },
                    floatingActionButtonPosition = FabPosition.End,
                    isFloatingActionButtonDocked = true
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        MainNavHost(
                            navHostController = navHostController,
                            onShowLinkActivity = ::launchLinkActivity,
                            onShowMoreActivity = ::launchMoreActivity
                        )
                    }
                }
            }
        }
    }
}