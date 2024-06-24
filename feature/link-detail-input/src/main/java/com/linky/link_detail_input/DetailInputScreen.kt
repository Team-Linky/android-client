package com.linky.link_detail_input

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.R
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.link_detail_input.animation.exitTransition
import com.linky.link_detail_input.component.DetailInputContent
import com.linky.link_detail_input.component.DetailInputHeader
import com.linky.link_detail_input.component.TagDeleteDialog
import com.linky.link_detail_input.component.TagDeleteDialogDirector
import com.linky.link_detail_input.component.TagDeleteDialogDirector.Companion.of
import com.linky.link_detail_input.state.DetailInputSideEffect
import com.linky.link_detail_input.state.LinkSaveStatus
import com.linky.link_detail_input.state.Mode
import com.linky.link_detail_input.state.OpenGraphStatus
import com.linky.model.Link
import com.linky.model.Tag
import com.linky.navigation.link.LinkNavType
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavController.navigatorDetailInput(
    url: String,
    mode: Int,
    linkId: Long
) {
    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    val route = LinkNavType.DetailInput.route
        .replace("{url}", encodedUrl)
        .replace("{mode}", mode.toString())
        .replace("{linkId}", linkId.toString())

    navigate(route = route) {
        popUpTo("startDestination") { inclusive = true }
    }
}

fun NavGraphBuilder.detailInputScreen(
    scaffoldState: ScaffoldState,
    onComplete: () -> Unit,
    onBack: () -> Unit,
) {
    composable(
        route = LinkNavType.DetailInput.route,
        arguments = listOf(
            navArgument("url") { type = NavType.StringType },
            navArgument("mode") { type = NavType.IntType },
            navArgument("linkId") { type = NavType.LongType }
        ),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = { exitTransition }
    ) {
        DetailInputRoute(
            scaffoldState = scaffoldState,
            onComplete = onComplete,
            onBack = onBack
        )
    }
}

@Composable
private fun DetailInputRoute(
    scaffoldState: ScaffoldState,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    DetailInputScreen(
        scaffoldState = scaffoldState,
        onComplete = onComplete,
        onBack = onBack
    )
}

@Composable
private fun DetailInputScreen(
    viewModel: DetailInputViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    onComplete: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.collectAsState()
    val tags = state.tags.collectAsLazyPagingItems()
    val mode = state.mode
    val openGraphStatus = state.openGraphStatus
    val linkSaveStatus = state.linkSaveStatus
    val link = state.link

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val memoFocusRequester = remember { FocusRequester() }
    val tagFocusRequester = remember { FocusRequester() }

    var memoText by remember(link) { mutableStateOf(link?.memo ?: "") }
    var tagText by rememberSaveable { mutableStateOf("") }

    val tagStore = remember { mutableStateMapOf<Tag, Boolean>() }
    val selectedTags by remember(tagStore) {
        derivedStateOf { tagStore.filter { it.value }.map { it.key } }
    }

    var showLoading by rememberSaveable { mutableStateOf(false) }

    var tagDeleteDialogDirector by remember { mutableStateOf(TagDeleteDialogDirector.Init) }

    TagDeleteDialog(
        director = tagDeleteDialogDirector,
        onCancel = { tagDeleteDialogDirector = TagDeleteDialogDirector.Init },
        onDelete = { tag ->
            viewModel.doAction(DetailInputAction.DeleteTag(tag.id!!))
            tagDeleteDialogDirector = TagDeleteDialogDirector.Init
        }
    )

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is DetailInputSideEffect.TagTextClear -> {
                tagText = ""
            }
        }
    }

    LaunchedEffect(linkSaveStatus) {
        showLoading = linkSaveStatus is LinkSaveStatus.Loading

        if (linkSaveStatus is LinkSaveStatus.Loading) {
            onComplete.invoke()
        }

        if (linkSaveStatus is LinkSaveStatus.Error) {
            scaffoldState.snackbarHostState.showSnackbar(
                ContextCompat.getString(
                    context.applicationContext,
                    R.string.link_detail_input_save_error
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DetailInputHeader(
            mode = mode,
            isNextActive = openGraphStatus is OpenGraphStatus.Success,
            onComplete = {
                val newLink = link?.copy(
                    memo = memoText,
                    tags = selectedTags,
                ) ?: Link(
                    memo = memoText,
                    tags = selectedTags,
                    openGraphData = (state.openGraphStatus as OpenGraphStatus.Success).openGraphData
                )
                viewModel.doAction(DetailInputAction.SaveLink(newLink))
            },
            onBack = {
                if (mode == Mode.Creator) {
                    onBack.invoke()
                } else {
                    (context as Activity).finish()
                }
            }
        )
        DetailInputContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            tags = tags,
            openGraphStatus = openGraphStatus,
            selectTags = selectedTags,
            memoValue = memoText,
            memoOnValueChange = { value ->
                memoText = if (value.length > 8) {
                    value.substring(0, 8)
                } else {
                    value
                }
            },
            memoOnClear = { memoText = "" },
            memoFocusRequester = memoFocusRequester,
            tagValue = tagText,
            tagOnValueChange = { tagText = it },
            tagOnClear = { tagText = "" },
            tagFocusRequester = tagFocusRequester,
            focusManager = focusManager,
            onSelectTag = { tagStore[it] = true },
            onUnSelectTag = { tagStore[it] = false },
            onDeleteTag = { tagDeleteDialogDirector = it.of() },
            onCreateTag = { viewModel.doAction(DetailInputAction.AddTag(it)) },
        )
    }

    if (showLoading) {
        LoadingDialog()
    }
}

@Composable
private fun LoadingDialog(
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}