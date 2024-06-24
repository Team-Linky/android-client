package com.linky.timeline

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.sp
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
import com.linky.link.extension.launchLinkActivity
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
    val links = state.links.collectAsLazyPagingItems()
    val clipboard = LocalClipboardManager.current
    val imageLoader = rememberImageLoader()
    val listState = rememberLazyListState()

    val showScrollTop by remember(listState) {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    TimeLineScreen(
        links = links,
        listState = listState,
        imageLoader = imageLoader,
        sortType = state.sortType,
        sorts = state.sortList,
        showScrollTop = showScrollTop,
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
        onChangeSort = { viewModel.doAction(TimeLineAction.ChangeSort(it)) },
        onEdit = onEdit
    )
}

@Composable
private fun TimeLineScreen(
    links: LazyPagingItems<Link>,
    listState: LazyListState,
    imageLoader: ImageLoader,
    sortType: Sort,
    sorts: List<Sort>,
    showScrollTop: Boolean,
    onShowLinkActivity: () -> Unit,
    onShowWebView: (Link) -> Unit,
    onEdit: (Link) -> Unit,
    onRemoveTimeLine: (Long) -> Unit,
    onCopyLink: (Link) -> Unit,
    onScrollTop: () -> Unit,
    onChangeSort: (Sort) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeLineHeader(
            sorts = sorts,
            sortType = sortType,
            onChangeSort = onChangeSort,
        )
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
                        when (sortType) {
                            is Sort.All -> {
                                TimeLineEmptyScreen(
                                    onShowLinkActivity = onShowLinkActivity
                                )
                            }

                            is Sort.NoRead -> {
                                Column {
                                    Spacer(modifier = Modifier.weight(0.3f))
                                    LinkyText(
                                        modifier = Modifier.weight(0.7f),
                                        text = stringResource(R.string.link_no_read_empty),
                                        color = ColorFamilyGray800AndGray300,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp,
                                    )
                                }
                            }

                            is Sort.Read -> {
                                Column {
                                    Spacer(modifier = Modifier.weight(0.3f))
                                    LinkyText(
                                        modifier = Modifier.weight(0.7f),
                                        text = stringResource(R.string.link_read_empty),
                                        color = ColorFamilyGray800AndGray300,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp,
                                    )
                                }
                            }
                        }
                    }
                }

                is LoadState.Error -> {

                }
            }

            if (links.itemSnapshotList.isNotEmpty()) {
                TimeLineList(
                    state = listState,
                    imageLoader = imageLoader,
                    links = links,
                    onEdit = onEdit,
                    onRemove = onRemoveTimeLine,
                    onClick = onShowWebView,
                    onCopyLink = onCopyLink,
                )
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
            links = defaultLinks,
            listState = rememberLazyListState(),
            imageLoader = rememberImageLoader(),
            sortType = Sort.All,
            sorts = listOf(Sort.All),
            showScrollTop = false,
            onShowLinkActivity = {},
            onShowWebView = {},
            onRemoveTimeLine = {},
            onCopyLink = {},
            onScrollTop = {},
            onChangeSort = {},
            onEdit = {}
        )
    }
}

private val defaultLinks
    @Composable
    get() = flowOf(PagingData.empty<Link>()).collectAsLazyPagingItems()