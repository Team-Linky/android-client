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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.RandomColor
import com.linky.design_system.ui.theme.ShadowBlack
import com.linky.design_system.ui.theme.White
import com.linky.design_system.util.clickableRipple
import com.linky.model.Tag
import com.linky.navigation.MainNavType
import com.linky.tag.animation.enterTransition
import com.linky.tag.animation.exitTransition
import com.linky.tag.component.TagEmptyScreen
import com.linky.tag.component.TagHeader

fun NavGraphBuilder.tagScreen(onShowLinkActivity: () -> Unit) {
    composable(
        route = MainNavType.Tag.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) {
        TagRoute(
            onShowLinkActivity = onShowLinkActivity
        )
    }
}

@Composable
private fun TagRoute(
    tagViewModel: TagViewModel = hiltViewModel(),
    onShowLinkActivity: () -> Unit,
) {
    val tags = tagViewModel.tagsState.collectAsLazyPagingItems()

    TagScreen(
        onShowLinkActivity = onShowLinkActivity,
        tags = tags
    )
}

@Composable
private fun TagScreen(
    onShowLinkActivity: () -> Unit = {},
    tags: LazyPagingItems<Tag>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TagHeader()

        when (tags.loadState.refresh) {
            is LoadState.Loading -> {
                if (tags.itemSnapshotList.isEmpty()) {
                    LinearProgressIndicator()
                }
            }

            is LoadState.NotLoading -> {
                if (tags.itemSnapshotList.isEmpty()) {
                    TagEmptyScreen(
                        modifier = Modifier.weight(1f),
                        onShowLinkActivity = onShowLinkActivity,
                    )
                }
            }

            is LoadState.Error -> {}
        }

        if (tags.itemSnapshotList.isNotEmpty()) {
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
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(bottom = 10.dp, start = 4.dp, end = 4.dp)
                                .size(160.dp, 110.dp)
                                .background(RandomColor, RoundedCornerShape(12.dp))
                                .clickableRipple(radius = 80.dp) { tag }
                        ) {
                            LinkyText(
                                text = tag.name,
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