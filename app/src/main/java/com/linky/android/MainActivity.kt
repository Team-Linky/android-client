package com.linky.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.linky.certification.extension.launchCertificationActivity
import com.linky.design_system.ui.component.floating.LinkyFloatingActionButton
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.link.extension.launchLinkActivity
import com.linky.more_activity.extension.launchMoreActivity
import com.linky.navigation.LinkyBottomNavigation
import com.linky.process_lifecycle.ProcessLifecycle
import com.sun5066.common.safe_coroutine.builder.safeLaunch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.processLifecycleEvent
            .onEach { processLifecycle ->
                when (processLifecycle) {
                    is ProcessLifecycle.Background -> lockTimerStart()
                    is ProcessLifecycle.Foreground -> checkLock()
                }
            }.launchIn(lifecycleScope)

        setContent {
            val navHostController = rememberAnimatedNavController()
            val scaffoldState = rememberScaffoldState()
            LinkyDefaultTheme {
                Scaffold(
                    bottomBar = { LinkyBottomNavigation(navHostController) },
                    floatingActionButton = { LinkyFloatingActionButton(::launchLinkActivity) },
                    floatingActionButtonPosition = FabPosition.End,
                    isFloatingActionButtonDocked = true,
                    scaffoldState = scaffoldState,
                ) { paddingValues ->
                    Surface(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        MainNavHost(
                            scaffoldState = scaffoldState,
                            navHostController = navHostController,
                            onShowLinkActivity = ::launchLinkActivity,
                            onShowMoreActivity = ::launchMoreActivity
                        )
                    }
                }
            }
        }
    }

    private var lockJob: Job? = null
    private var lockTimerCnt = 0

    private fun checkLock() {
        lockJob?.takeIf { it.isActive }?.cancel()

        lifecycleScope.safeLaunch {
            if (lockTimerCnt >= 3) {
                if (viewModel.getEnableLock()) {
                    launchCertificationActivity()
                }
            }
        }
    }

    private fun lockTimerStart() {
        lockTimerCnt = 0
        lockJob = lifecycleScope.safeLaunch {
            (0..4).onEach {
                lockTimerCnt++
                delay(1000)
            }
        }
    }

}