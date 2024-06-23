package com.linky.feature.tag_modifier

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.R
import com.linky.design_system.ui.component.chip.LinkyTagChip
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.LinkyUrlInputTextField
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.Gray600
import com.linky.feature.tag_modifier.component.TagModifierHeader
import com.linky.feature.tag_modifier.state.Mode
import com.linky.model.TagWithUsage
import org.orbitmvi.orbit.compose.collectAsState

fun NavHostController.navigatorTagModifierScreen(linkId: Long, mode: Mode) =
    navigate(route = "tagModifierScreen/$linkId/${mode.ordinal}")

fun NavGraphBuilder.tagModifierScreen(onBack: () -> Unit) {
    composable(
        route = "tagModifierScreen/{linkId}/{mode}",
        arguments = listOf(
            navArgument("linkId") { type = NavType.LongType },
            navArgument("mode") { type = NavType.IntType },
        ),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {
        TagModifierRoute(
            onBack = onBack
        )
    }
}

@Composable
private fun TagModifierRoute(
    viewModel: TagModifierViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.collectAsState()
    val tags = state.tags.collectAsLazyPagingItems()

    TagModifierScreen(
        mode = state.mode,
        tags = tags,
        onCreateTag = { viewModel.doAction(Action.InsertTag(it)) },
        onBack = onBack,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagModifierScreen(
    mode: Mode,
    tags: LazyPagingItems<TagWithUsage>,
    onCreateTag: (String) -> Unit,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var tag by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val tagStore = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TagModifierHeader(
            isActive = false,
            onComplete = onBack,
            onBack = onBack
        )

        Spacer(modifier = Modifier.weight(0.2f))

        Column(
            modifier = Modifier.padding(horizontal = 46.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (mode) {
                Mode.None -> {
                    CircularProgressIndicator()
                }

                Mode.Creator -> {
                    LinkyText(
                        text = stringResource(R.string.tag_add),
                        color = ColorFamilyGray900AndGray100,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(56.dp))
                }

                Mode.Editor -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinkyText(
                            text = stringResource(R.string.tag_edit),
                            color = ColorFamilyGray900AndGray100,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinkyText(
                            text = stringResource(R.string.tag_modifier_desc),
                            color = Gray600,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

            }

            LinkyUrlInputTextField(
                value = tag,
                placeholder = stringResource(R.string.tag_modifier_placeholder),
                onValueChange = { value ->
                    tag = if (value.length > 8) {
                        value.substring(0, 8)
                    } else {
                        value
                    }
                },
                onClear = { tag = "" },
                focusRequester = focusRequester,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onCreateTag.invoke(tag)
                        tag = ""
                        focusManager.clearFocus()
                    }
                )
            )

            val newTags = remember { mutableStateListOf<TagWithUsage>() }

            LaunchedEffect(tags.loadState) {
                if (tags.loadState.refresh is LoadState.NotLoading) {
                    if (tags.itemSnapshotList.isNotEmpty() && newTags.size != tags.itemCount) {
                        val start = if (newTags.isEmpty()) 0 else newTags.size - 1
                        val end = tags.itemCount - 1

                        (start..<end).forEach { index ->
                            tags[index]?.let { newTags += it }
                        }
                    }
                }
            }

            if (tags.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator()
            } else if (tags.loadState.refresh is LoadState.NotLoading) {
                Spacer(modifier = Modifier.height(16.dp))

                FlowRow {
                    (0..<tags.itemCount).forEach { index ->
                        tags[index]?.let { tagWithUsage ->
                            val isSelected by remember(tagStore[tagWithUsage.tag.name]) {
                                derivedStateOf { tagStore[tagWithUsage.tag.name] ?: tagWithUsage.isUsed }
                            }

                            Box(
                                modifier = Modifier.padding(end = 4.dp, bottom = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                LinkyTagChip(
                                    text = tagWithUsage.tag.name,
                                    isSelected = isSelected,
                                    onClick = { tagStore[tagWithUsage.tag.name] = !isSelected }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Flow(
    items: LazyPagingItems<TagWithUsage>,
    maxItemsInEachRow: Int = Int.MAX_VALUE
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val rows = items.itemCount / maxItemsInEachRow + if (items.itemCount % maxItemsInEachRow > 0) 1 else 0
        items(rows) { rowIndex ->
            LazyRow {
                items(maxItemsInEachRow) { columnIndex ->
                    val itemIndex = rowIndex * maxItemsInEachRow + columnIndex
                    if (itemIndex < items.itemCount) {
                        val item = items[itemIndex]
                        item?.let {

                        }
                    }
                }
            }
        }
    }
}