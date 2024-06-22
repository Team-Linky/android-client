package com.linky.tag

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.ImageLoader
import com.google.accompanist.navigation.animation.composable
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray800
import com.linky.design_system.ui.theme.RandomColor
import com.linky.design_system.ui.theme.ShadowBlack
import com.linky.design_system.ui.theme.White
import com.linky.design_system.util.clickableRipple
import com.linky.design_system.util.rememberImageLoader
import com.linky.model.Link
import com.linky.model.Tag
import com.linky.navigation.MainNavType
import com.linky.tag.animation.enterTransition
import com.linky.tag.animation.exitTransition
import com.linky.tag.component.LinkySearchTextField
import com.linky.tag.component.TagEmptyScreen
import com.linky.tag.component.TagHeader
import com.linky.tag.component.TimeLineList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import org.orbitmvi.orbit.compose.collectAsState

fun NavGraphBuilder.tagScreen(
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit
) {
    composable(
        route = MainNavType.Tag.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) {
        TagRoute(
            scaffoldState = scaffoldState,
            onShowLinkActivity = onShowLinkActivity
        )
    }
}

@Composable
private fun TagRoute(
    viewModel: TagViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    onShowLinkActivity: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val state by viewModel.collectAsState()
    val tags = state.tags.collectAsLazyPagingItems()
    val links = state.links.collectAsLazyPagingItems()

    val imageLoader = rememberImageLoader()
    val listState = rememberLazyListState()

    TagScreen(
        tags = tags,
        links = links,
        imageLoader = imageLoader,
        listState = listState,
        searchTag = state.searchTag,
        onShowLinkActivity = onShowLinkActivity,
        onSearchTag = { viewModel.doAction(Action.SearchTimeLine(it)) },
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
                        context.applicationContext,
                        R.string.copy_complete
                    )
                )
            }
        },
        onScrollTop = {
            coroutineScope.safeLaunch { listState.animateScrollToItem(0) }
        },
    )
}

@OptIn(ExperimentalMaterialApi::class, FlowPreview::class)
@Composable
private fun TagScreen(
    tags: LazyPagingItems<Tag>,
    links: LazyPagingItems<Link>,
    imageLoader: ImageLoader,
    listState: LazyListState,
    searchTag: String,
    onShowLinkActivity: () -> Unit,
    onSearchTag: (String) -> Unit,
    onCopyLink: (Link) -> Unit,
    onScrollTop: () -> Unit,
) {
    var isExpandable by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf(searchTag) }
    val searchFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val isRefreshLoadingTags by remember(tags.loadState) {
        derivedStateOf {
            tags.loadState.refresh is LoadState.Loading && tags.itemSnapshotList.isEmpty()
        }
    }

    var tagSearching by rememberSaveable { mutableStateOf(false) }

    val isRefreshLoadingLinks by remember(links.loadState) {
        derivedStateOf {
            links.loadState.refresh is LoadState.Loading && links.itemSnapshotList.isEmpty()
        }
    }

    val showEmpty by remember(tags.loadState) {
        derivedStateOf {
            tags.loadState.refresh is LoadState.NotLoading && tags.itemSnapshotList.isEmpty()
        }
    }

    LaunchedEffect(searchText) {
        if (searchText != searchTag) {
            snapshotFlow { searchText }
                .filter { it.isNotBlank() }
                .debounce(500L)
                .collectLatest {
                    tagSearching = true
                    onSearchTag.invoke(it)
                }
        }

        if (searchText.isEmpty()) {
            tagSearching = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TagHeader(
            expanded = isExpandable,
            onChangeExpanded = { isExpandable = it }
        )
        AnimatedVisibility(visible = isExpandable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 16.dp)
                    .height(60.dp)
                    .addFocusCleaner(focusManager),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkySearchTextField(
                    modifier = Modifier
                        .clickable { searchFocusRequester.requestFocus() }
                        .weight(1f)
                        .height(38.dp)
                        .background(ColorFamilyWhiteAndGray800, RoundedCornerShape(12.dp))
                        .padding(start = 6.dp, end = 10.dp),
                    value = searchText,
                    placeholder = stringResource(R.string.tag_search_placeholder),
                    focusManager = focusManager,
                    focusRequester = searchFocusRequester,
                    onValueChange = { searchText = it },
                    onClear = { searchText = "" },
                )
                Spacer(modifier = Modifier.width(6.dp))
                LinkyText(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                        .clickableRipple(radius = 12.dp) { isExpandable = false },
                    text = stringResource(R.string.cancel),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorFamilyGray600AndGray400
                )
            }
        }

        if (tagSearching && searchText == searchTag) {
            if (isRefreshLoadingLinks) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                    content = { LinearProgressIndicator() }
                )
            } else {
                TimeLineList(
                    state = listState,
                    imageLoader = imageLoader,
                    links = links,
                    onClick = {},
                    onCopyLink = onCopyLink,
                )
            }
        } else {
            if (isRefreshLoadingTags) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                    content = { LinearProgressIndicator() }
                )
            } else {
                if (showEmpty) {
                    TagEmptyScreen(
                        modifier = Modifier.weight(1f),
                        onShowLinkActivity = onShowLinkActivity,
                    )
                } else if (tags.itemSnapshotList.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.FixedSize(160.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(
                            count = tags.itemCount,
                            key = tags.itemKey { it.id ?: 0L },
                            contentType = tags.itemContentType { "TagItems" }
                        ) { index ->
                            tags[index]?.let { tag ->
                                Card(
                                    modifier = Modifier
                                        .size(160.dp, 110.dp)
                                        .padding(bottom = 10.dp, start = 4.dp, end = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    backgroundColor = RandomColor,
                                    onClick = { searchText = tag.name }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LinkyText(
                                            text = "# ${tag.name}",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = White,
                                            style = TextStyle(
                                                shadow = Shadow(
                                                    color = ShadowBlack,
                                                    offset = Offset(0f, 0f),
                                                    blurRadius = 10f
                                                )
                                            )
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
}