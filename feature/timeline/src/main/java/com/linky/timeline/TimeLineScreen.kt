package com.linky.timeline

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.navigation.MainNavType
import com.linky.timeline.animation.enterTransition
import com.linky.timeline.animation.exitTransition
import com.linky.timeline.component.TimeLineContent
import com.linky.timeline.component.TimeLineHeader
import com.linky.webview.extension.launchWebViewActivity
import kotlinx.coroutines.launch

fun NavGraphBuilder.timelineScreen(
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit
) {
    composable(
        route = MainNavType.TimeLine.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) { TimeLineRoute(scaffoldState, onShowLinkActivity) }
}

@Composable
private fun TimeLineRoute(
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit
) {
    TimeLineScreen(scaffoldState, onShowLinkActivity)
}

@Composable
private fun TimeLineScreen(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onShowLinkActivity: () -> Unit = {},
    viewModel: TimeLineViewModel = hiltViewModel()
) {
    val links = viewModel.linkList.collectAsLazyPagingItems()
    val activity = LocalContext.current as ComponentActivity
    val clipboard = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeLineHeader()
        TimeLineContent(
            onShowLinkActivity = onShowLinkActivity,
            onClick = { link ->
                link.openGraphData.url?.also { url ->
                    activity.launchWebViewActivity(url)
                    viewModel.incrementReadCount(link.id!!)
                }
            },
            onEdit = {  },
            onRemove = { viewModel.setIsRemoveUseCase(it, true) },
            onCopyLink = { link ->
                coroutineScope.launch {
                    clipboard.setText(
                        AnnotatedString
                            .Builder()
                            .append(link.openGraphData.url)
                            .toAnnotatedString()
                    )
                    scaffoldState.snackbarHostState.showSnackbar("링크가 복사되었습니다.")
                }
            },
            links = links
        )
    }
}

@Preview
@Composable
private fun TimeLinePreview() {
    LinkyDefaultTheme {
        TimeLineScreen()
    }
}