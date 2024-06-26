package com.linky.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.design_system.ui.theme.ShadowBlack
import com.linky.design_system.ui.theme.White
import com.linky.design_system.util.clickableRipple
import com.linky.model.Tag
import com.linky.navigation.MainNavType
import com.linky.tag.animation.enterTransition
import com.linky.tag.animation.exitTransition
import com.linky.tag.component.TagEmptyScreen
import com.linky.tag.component.TagHeader
import com.linky.tag.state.TagState
import org.orbitmvi.orbit.compose.collectAsState

fun NavGraphBuilder.tagScreen(
    onShowLinkActivity: () -> Unit,
    onShowTimeLineActivity: (String) -> Unit,
) {
    composable(
        route = MainNavType.Tag.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) {
        TagRoute(
            onShowLinkActivity = onShowLinkActivity,
            onShowTimeLineActivity = onShowTimeLineActivity
        )
    }
}

@Composable
private fun TagRoute(
    viewModel: TagViewModel = hiltViewModel(),
    onShowLinkActivity: () -> Unit,
    onShowTimeLineActivity: (String) -> Unit,
) {
    val state by viewModel.collectAsState()

    TagScreen(
        state = state,
        onShowLinkActivity = onShowLinkActivity,
        onShowTimeLineActivity = onShowTimeLineActivity,
    )
}

@Composable
private fun TagScreen(
    state: TagState,
    onShowLinkActivity: () -> Unit,
    onShowTimeLineActivity: (String) -> Unit,
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var showLoading by rememberSaveable { mutableStateOf(false) }
    var showTagScreen by rememberSaveable { mutableStateOf(false) }

    val tags = state.tags.collectAsLazyPagingItems()

    val newTags by remember(tags) {
        derivedStateOf {
            tags.itemSnapshotList
                .filter { it?.name?.contains(searchText) == true || searchText.isEmpty() }
                .filterNotNull()
        }
    }

    LaunchedEffect(tags.loadState) {
        showLoading = tags.loadState.refresh is LoadState.Loading
        showTagScreen = tags.loadState.refresh is LoadState.NotLoading
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TagHeader(
            searchValue = searchText,
            onValueChange = { searchText = it },
            onClear = { searchText = "" },
            focusManager = focusManager,
            focusRequester = focusRequester,
        )

        if (showLoading) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
                content = { LinearProgressIndicator() }
            )
        }

        if (tags.itemSnapshotList.isEmpty()) {
            TagEmptyScreen(
                modifier = Modifier.weight(1f),
                onShowLinkActivity = onShowLinkActivity,
            )
        }

        LazyVerticalGrid(
            columns = GridCells.FixedSize(160.dp),
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            items(
                items = newTags,
                key = { it.id ?: 0L },
                contentType = { "TagItems" }
            ) { tag ->
                Card(
                    modifier = Modifier
                        .size(160.dp, 110.dp)
                        .padding(bottom = 10.dp, start = 4.dp, end = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = tag.color,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickableRipple(
                                enableRipple = false,
                                onClick = { onShowTimeLineActivity.invoke(tag.name) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        LinkyText(
                            text = "# ${tag.name}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.dp,
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

private val Tag.color
    get() = Color(
        red = red,
        green = green,
        blue = blue,
    )