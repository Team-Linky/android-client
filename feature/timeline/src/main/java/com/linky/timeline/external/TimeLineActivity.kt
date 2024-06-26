package com.linky.timeline.external

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.design_system.R
import com.linky.design_system.animation.slideIn
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.design_system.ui.theme.Nav700
import com.linky.design_system.util.clickableRipple
import com.linky.design_system.util.rememberImageLoader
import com.linky.link.extension.launchLinkActivity
import com.linky.link.extension.rememberLaunchLinkActivityResult
import com.linky.timeline.TimeLineAction
import com.linky.timeline.TimeLineEmptyScreen
import com.linky.timeline.TimeLineViewModel
import com.linky.timeline.component.TimeLineList
import com.linky.timeline.component.TimeLineMenuBox
import com.linky.timeline.state.Sort
import com.linky.webview.extension.launchWebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState

internal const val INTENT_KEY_TAG_NAME = "tagName"

fun ComponentActivity.launchTimeLineActivity(tagName: String? = null) {
    Intent(this, TimeLineActivity::class.java).apply {
        putExtra(INTENT_KEY_TAG_NAME, tagName)
        startActivity(this)
        slideIn()
    }
}

@AndroidEntryPoint
class TimeLineActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinkyDefaultTheme {
                val viewModel = hiltViewModel<TimeLineViewModel>()
                val scaffoldState = rememberScaffoldState()
                val state by viewModel.collectAsState()
                val linksPager = state.links.collectAsLazyPagingItems()
                var sortType by remember { mutableStateOf(Sort.All) }

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

                val coroutineScope = rememberCoroutineScope()
                val clipboard = LocalClipboardManager.current
                val imageLoader = rememberImageLoader()
                val listState = rememberLazyListState()

                val showScrollTop by remember(listState) {
                    derivedStateOf { listState.firstVisibleItemIndex > 0 }
                }

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

                val showLoading by remember(linksPager.loadState.refresh) {
                    derivedStateOf { linksPager.loadState.refresh is LoadState.Loading && linksPager.itemCount == 0 }
                }

                val showEmpty by remember(linksPager.loadState.refresh, links) {
                    derivedStateOf { linksPager.loadState.refresh is LoadState.NotLoading && links.isEmpty() }
                }

                val showAppending by remember(linksPager.loadState.append) {
                    derivedStateOf { linksPager.loadState.append is LoadState.Loading && linksPager.itemCount > 0 }
                }

                Scaffold(scaffoldState = scaffoldState) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        LinkyHeader(
                            modifier = Modifier.padding(start = 12.dp, end = 16.dp)
                        ) {
                            LinkyBackArrowButton(
                                onClick = ::finish
                            )
                            intent.getStringExtra(INTENT_KEY_TAG_NAME)?.let { tagName ->
                                LinkyText(
                                    text = tagName,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.dp,
                                    color = ColorFamilyGray900AndGray100,
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                            TimeLineMenuBox(
                                sorts = arrayOf(Sort.All, Sort.Read, Sort.NoRead),
                                sortType = sortType,
                                onChangeSort = { sortType = it }
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
                                    imageLoader = imageLoader,
                                    showAppending = showAppending,
                                    onEdit = { link ->
                                        linkLauncher.launchLinkActivity(
                                            activity = this@TimeLineActivity,
                                            startDestination = "link_edit",
                                            mode = 2,
                                            url = link.openGraphData.url,
                                            linkId = link.id,
                                        )
                                    },
                                    onRemove = { viewModel.doAction(TimeLineAction.RemoveTimeLine(it)) },
                                    onClick = { link ->
                                        link.openGraphData.url?.also { url ->
                                            launchWebViewActivity(url)
                                            viewModel.doAction(TimeLineAction.IncrementReadCount(link.id))
                                        }
                                    },
                                    onCopyLink = { link ->
                                        coroutineScope.safeLaunch {
                                            clipboard.setText(
                                                AnnotatedString
                                                    .Builder()
                                                    .append(link.openGraphData.url)
                                                    .toAnnotatedString()
                                            )
                                            scaffoldState.snackbarHostState.showSnackbar(getString(R.string.copy_complete))
                                        }
                                    },
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
                                            onShowLinkActivity = ::launchLinkActivity
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
                                            .clickableRipple {
                                                coroutineScope.safeLaunch {
                                                    listState.animateScrollToItem(0)
                                                }
                                            },
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