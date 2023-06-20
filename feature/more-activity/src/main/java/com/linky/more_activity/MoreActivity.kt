package com.linky.more_activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.theme.LinkyLinkTheme
import com.linky.link.extension.launchLinkActivity
import com.linky.more_activity.extension.startDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            LinkyLinkTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MoreNavHost(
                        navHostController = navHostController,
                        startDestination = startDestination!!,
                        onClose = ::finish,
                        onLinkCreate = ::launchLinkActivity
                    )
                }
            }
        }
    }

    override fun onPause() {
        if (isFinishing) {
            slideOut()
        }
        super.onPause()
    }

}