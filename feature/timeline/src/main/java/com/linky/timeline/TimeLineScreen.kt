package com.linky.timeline

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.ImageLoader
import com.google.accompanist.navigation.animation.composable
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.design_system.ui.theme.Nav700
import com.linky.design_system.util.clickableRipple
import com.linky.design_system.util.rememberImageLoader
import com.linky.model.Link
import com.linky.navigation.MainNavType
import com.linky.timeline.animation.enterTransition
import com.linky.timeline.animation.exitTransition
import com.linky.timeline.component.TimeLineHeader
import com.linky.timeline.component.TimeLineList
import com.linky.timeline.state.Sort
import com.linky.webview.extension.launchWebViewActivity
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.compose.collectAsState

fun NavGraphBuilder.timelineScreen(
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit,
    onEdit: (Link) -> Unit,
) {
    composable(
        route = MainNavType.TimeLine.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) {
        TimeLineRoute(
            scaffoldState = scaffoldState,
            onShowLinkActivity = onShowLinkActivity,
            onEdit = onEdit
        )
    }
}

@Composable
private fun TimeLineRoute(
    viewModel: TimeLineViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit,
    onEdit: (Link) -> Unit,
) {
    val activity = LocalContext.current as ComponentActivity
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.collectAsState()
    val linksPager = state.links.collectAsLazyPagingItems()
    val clipboard = LocalClipboardManager.current
    val imageLoader = rememberImageLoader()
    val listState = rememberLazyListState()
    var sortType by remember { mutableStateOf(Sort.All) }

    val showScrollTop by remember(listState) {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    TimeLineScreen(
        linksPager = linksPager,
        sortType = sortType,
        listState = listState,
        imageLoader = imageLoader,
        showScrollTop = showScrollTop,
        onChangeSort = { sortType = it },
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
                scaffoldState.snackbarHostState.showSnackbar(
                    ContextCompat.getString(
                        activity.applicationContext,
                        R.string.copy_complete
                    )
                )
            }
        },
        onScrollTop = {
            coroutineScope.safeLaunch { listState.animateScrollToItem(0) }
        },
        onEdit = onEdit
    )
}

@Composable
private fun TimeLineScreen(
    linksPager: LazyPagingItems<Link>,
    listState: LazyListState,
    imageLoader: ImageLoader,
    sortType: Sort,
    showScrollTop: Boolean,
    onChangeSort: (Sort) -> Unit,
    onShowLinkActivity: () -> Unit,
    onShowWebView: (Link) -> Unit,
    onEdit: (Link) -> Unit,
    onRemoveTimeLine: (Long) -> Unit,
    onCopyLink: (Link) -> Unit,
    onScrollTop: () -> Unit,
) {
    val links by remember(linksPager, sortType) {
        derivedStateOf {
            linksPager.itemSnapshotList.items.filter { link ->
                when (sortType) {
                    Sort.All -> true
                    Sort.Read -> !link.isNoRead
                    Sort.NoRead -> link.isNoRead
                }
            }
        }
    }

    val showLoading by remember(linksPager.loadState.refresh) {
        derivedStateOf { linksPager.loadState.refresh is LoadState.Loading && linksPager.itemCount == 0 }
    }

    val showEmpty by remember(linksPager.loadState.refresh, links) {
        derivedStateOf { linksPager.loadState.refresh is LoadState.NotLoading && links.isEmpty() }
    }

    val showAppending by remember(linksPager.loadState.append) {
        derivedStateOf { linksPager.loadState.append is LoadState.Loading && linksPager.itemCount > 0 }
    }

    val showHeader by remember(listState) {
        derivedStateOf { listState.firstVisibleItemIndex < 1 }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = showHeader) {
            TimeLineHeader(
                sortType = sortType,
                onChangeSort = onChangeSort,
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = !showEmpty,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                TimeLineList(
                    links = links,
                    state = listState,
                    showAppending = showAppending,
                    imageLoader = imageLoader,
                    onEdit = onEdit,
                    onRemove = onRemoveTimeLine,
                    onClick = onShowWebView,
                    onCopyLink = onCopyLink,
                )
            }

            if (showLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = showEmpty,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                when (sortType) {
                    Sort.All -> {
                        TimeLineEmptyScreen(
                            onShowLinkActivity = onShowLinkActivity
                        )
                    }

                    Sort.NoRead -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier.weight(0.3f)
                            )
                            LinkyText(
                                modifier = Modifier.weight(0.7f),
                                text = stringResource(R.string.link_no_read_empty),
                                color = ColorFamilyGray800AndGray300,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.dp,
                            )
                        }
                    }

                    Sort.Read -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier.weight(0.3f)
                            )
                            LinkyText(
                                modifier = Modifier.weight(0.7f),
                                text = stringResource(R.string.link_read_empty),
                                color = ColorFamilyGray800AndGray300,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.dp,
                            )
                        }
                    }
                }
            }

            if (showScrollTop) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Nav700.copy(alpha = 0.7f))
                            .clickableRipple(onClick = onScrollTop),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.scroll_top_arrow),
                            contentDescription = "scroll top"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TimeLinePreview() {
    LinkyDefaultTheme {
        TimeLineScreen(
            linksPager = defaultLinks,
            listState = rememberLazyListState(),
            imageLoader = rememberImageLoader(),
            sortType = Sort.All,
            showScrollTop = false,
            onChangeSort = {},
            onShowLinkActivity = {},
            onShowWebView = {},
            onRemoveTimeLine = {},
            onCopyLink = {},
            onScrollTop = {},
            onEdit = {}
        )
    }
}

private val defaultLinks
    @Composable
    get() = flowOf(PagingData.empty<Link>()).collectAsLazyPagingItems()