package com.linky.timeline

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.model.Link
import com.linky.navigation.MainNavType
import com.linky.timeline.animation.enterTransition
import com.linky.timeline.animation.exitTransition
import com.linky.timeline.component.TimeLineHeader
import com.linky.timeline.component.TimeLineList
import com.linky.webview.extension.launchWebViewActivity
import com.sun5066.common.safe_coroutine.builder.safeLaunch
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.compose.collectAsState

fun NavGraphBuilder.timelineScreen(
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit
) {
    composable(
        route = MainNavType.TimeLine.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) {
        TimeLineRoute(
            scaffoldState = scaffoldState,
            onShowLinkActivity = onShowLinkActivity
        )
    }
}

@Composable
private fun TimeLineRoute(
    viewModel: TimeLineViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit
) {
    val activity = LocalContext.current as ComponentActivity
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.collectAsState()
    val links = state.links.collectAsLazyPagingItems()
    val clipboard = LocalClipboardManager.current

    TimeLineScreen(
        links = links,
        onShowLinkActivity = onShowLinkActivity,
        onShowWebView = { link ->
            link.openGraphData.url?.also { url ->
                activity.launchWebViewActivity(url)
                viewModel.doAction(TimeLineAction.IncrementReadCount(link.id))
            }
        },
        onRemoveTimeLine = { viewModel.doAction(TimeLineAction.RemoveTimeLine(it)) },
        onCopyLink = { link ->
            coroutineScope.safeLaunch {
                clipboard.setText(
                    AnnotatedString
                        .Builder()
                        .append(link.openGraphData.url)
                        .toAnnotatedString()
                )
                scaffoldState.snackbarHostState.showSnackbar("링크가 복사되었습니다.")
            }
        }
    )
}

@Composable
private fun TimeLineScreen(
    links: LazyPagingItems<Link>,
    onShowLinkActivity: () -> Unit,
    onShowWebView: (Link) -> Unit,
    onRemoveTimeLine: (Long) -> Unit,
    onCopyLink: (Link) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeLineHeader()
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            when (links.loadState.refresh) {
                is LoadState.Loading -> {
                    if (links.itemSnapshotList.isEmpty()) {
                        LinearProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is LoadState.NotLoading -> {
                    if (links.itemSnapshotList.isEmpty()) {
                        TimeLineEmptyScreen(
                            onShowLinkActivity = onShowLinkActivity
                        )
                    }
                }

                is LoadState.Error -> {

                }
            }

            if (links.itemSnapshotList.isNotEmpty()) {
                TimeLineList(
                    links = links,
                    onEdit = { },
                    onRemove = onRemoveTimeLine,
                    onClick = onShowWebView,
                    onCopyLink = onCopyLink,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TimeLinePreview() {
    LinkyDefaultTheme {
        TimeLineScreen(
            links = defaultLinks,
            onShowLinkActivity = {},
            onShowWebView = {},
            onRemoveTimeLine = {},
            onCopyLink = {},
        )
    }
}

private val defaultLinks
    @Composable
    get() = flowOf(PagingData.empty<Link>()).collectAsLazyPagingItems()