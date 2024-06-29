package com.linky.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.design_system.ui.component.floating.LinkyFloatingActionButton
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.feature.ask.ASK_ROUTE
import com.linky.feature.recycle_bin.launchRecycleBinActivity
import com.linky.feature.tag_setting.launchTagSettingActivity
import com.linky.link.LinkActivity
import com.linky.link.extension.launchLinkActivity
import com.linky.link.extension.rememberLaunchLinkActivityResult
import com.linky.more_activity.extension.launchMoreActivity
import com.linky.navigation.LinkyBottomNavigation
import com.linky.navigation.MainNavType
import com.linky.pin.extension.launchPinActivity
import com.linky.process_lifecycle.ProcessLifecycle
import com.linky.timeline.external.launchTimeLineActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private var lockJob: Job? = null
    private var lockTimerCnt = 0

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intentHandle(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intentHandle(intent)

        lifecycleScope.safeLaunch {
            if (viewModel.getEnableLock()) {
                launchPinActivity()
            }
        }

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
            val coroutineScope = rememberCoroutineScope()
            val linkLauncher = rememberLaunchLinkActivityResult(
                onCancel = {},
                onSuccess = { data ->
                    when (data?.getString("cmd")) {
                        "showSnackBar" -> {
                            coroutineScope.safeLaunch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    data.getString("msg", "")
                                )
                            }
                        }
                    }
                }
            )

            val backstack by navHostController.currentBackStackEntryAsState()

            val showBottomNav by remember(backstack?.destination?.route) {
                derivedStateOf {
                    MainNavType.containsRoute(backstack?.destination?.route ?: "")
                }
            }

            LinkyDefaultTheme {
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(visible = showBottomNav) {
                            LinkyBottomNavigation(navHostController)
                        }
                    },
                    floatingActionButton = {
                        if (showBottomNav) {
                            LinkyFloatingActionButton(::launchLinkActivity)
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    isFloatingActionButtonDocked = true,
                    scaffoldState = scaffoldState,
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        MainNavHost(
                            scaffoldState = scaffoldState,
                            navHostController = navHostController,
                            onShowLinkActivity = ::launchLinkActivity,
                            onShowTimeLineActivity = ::launchTimeLineActivity,
                            onShowMoreActivity = ::launchMoreActivity,
                            onShowTagSettingActivity = ::launchTagSettingActivity,
                            onShowLinkRecycleBinActivity = ::launchRecycleBinActivity,
                            onShowAskScreen = {
                                navHostController.navigate(ASK_ROUTE) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onEdit = { link ->
                                linkLauncher.launchLinkActivity(
                                    activity = this,
                                    startDestination = "link_edit",
                                    mode = 2,
                                    url = link.openGraphData.url,
                                    linkId = link.id,
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun intentHandle(intent: Intent) {
        if (intent.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                val linkIntent = Intent(this, LinkActivity::class.java).apply {
                    putExtra("startDestination", "link_edit")
                    putExtra("url", intent.getStringExtra(Intent.EXTRA_TEXT) ?: "")
                    putExtra("mode", 1)
                    putExtra("linkId", -1L)
                    putExtras(intent)
                }
                startActivity(linkIntent)
            }
        }
    }

    private fun checkLock() {
        lockJob?.takeIf { it.isActive }?.cancel()

        lifecycleScope.safeLaunch {
            if (lockTimerCnt >= 3) {
                if (viewModel.getEnableLock()) {
                    launchPinActivity()
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