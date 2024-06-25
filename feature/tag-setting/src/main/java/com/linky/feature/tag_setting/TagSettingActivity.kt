package com.linky.feature.tag_setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult.ActionPerformed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.design_system.R
import com.linky.design_system.animation.slideIn
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.button.LinkyButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray800
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray999
import com.linky.design_system.ui.theme.Gray300
import com.linky.design_system.ui.theme.LinkyLinkTheme
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.ui.theme.SubColor
import com.linky.design_system.util.clickableRipple
import com.linky.design_system.util.throttleClickRipple
import com.linky.feature.tag_setting.add.launchTagModifierActivity
import com.linky.feature.tag_setting.add.rememberLaunchTagModifierActivityResult
import com.linky.feature.tag_setting.component.DeleteTagConfirmDialog
import com.linky.feature.tag_setting.component.TagDeleteDialogDirector.Companion.Init
import com.linky.feature.tag_setting.state.SnackBarAction
import com.linky.feature.tag_setting.state.TagSettingSideEffect
import com.linky.model.Tag
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

fun ComponentActivity.launchTagSettingActivity() {
    Intent(this, TagSettingActivity::class.java).apply {
        startActivity(this)
        slideIn()
    }
}

@AndroidEntryPoint
class TagSettingActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinkyLinkTheme {
                val viewModel = hiltViewModel<TagSettingViewModel>()
                val state by viewModel.collectAsState()
                val tagWithLinkCounts = state.tagWithLinkCounts.collectAsLazyPagingItems()
                val scaffoldState = rememberScaffoldState()
                val coroutineScope = rememberCoroutineScope()

                val tagModifierActivityLauncher = rememberLaunchTagModifierActivityResult(
                    onSuccess = { intent ->
                        coroutineScope.safeLaunch {
                            val tagName = intent?.getStringExtra("tagName")
                            val mode = intent?.getStringExtra("cmd").let { cmd ->
                                when (cmd) {
                                    "edit" -> "수정"
                                    else -> "추가"
                                }
                            }
                            val message = String.format(getString(R.string.tag_add_complete), tagName, mode)

                            scaffoldState.snackbarHostState.showSnackbar(message)
                        }
                    }
                )

                var tagDeleteDialogDirector by remember { mutableStateOf(Init) }

                val isLoading by remember(tagWithLinkCounts.loadState) {
                    derivedStateOf { tagWithLinkCounts.loadState.refresh is LoadState.Loading }
                }

                val showList by remember(tagWithLinkCounts.loadState) {
                    derivedStateOf {
                        tagWithLinkCounts.loadState.refresh is LoadState.NotLoading &&
                                tagWithLinkCounts.itemSnapshotList.isNotEmpty()
                    }
                }

                val selectTags = remember { mutableStateListOf<Tag>() }
                val tagEditActive by remember(selectTags.size) {
                    derivedStateOf { selectTags.size == 1 }
                }
                val tagDeleteActive by remember(selectTags.size) {
                    derivedStateOf { selectTags.size > 0 }
                }

                viewModel.collectSideEffect { sideEffect ->
                    when (sideEffect) {
                        is TagSettingSideEffect.SnackBar -> {
                            val isUnDoDelete = sideEffect.snackBarAction is SnackBarAction.UnDoDelete
                            val actionLabel = if (isUnDoDelete) getString(R.string.delete_cancel) else null

                            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                                message = sideEffect.message,
                                actionLabel = actionLabel
                            )

                            if (snackBarResult == ActionPerformed) {
                                when (sideEffect.snackBarAction) {
                                    is SnackBarAction.UnDoDelete -> viewModel.doAction(TagSettingAction.UnDoDelete)
                                    else -> Unit
                                }
                            }
                        }

                        is TagSettingSideEffect.ClearSelectTagList -> {
                            selectTags.clear()
                        }
                    }
                }

                DeleteTagConfirmDialog(
                    director = tagDeleteDialogDirector,
                    onCancel = { tagDeleteDialogDirector = Init },
                    onDelete = {
                        viewModel.doAction(TagSettingAction.TagDelete(selectTags.toList()))
                        selectTags.clear()
                        tagDeleteDialogDirector = Init
                    }
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    backgroundColor = ColorFamilyWhiteAndGray999
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        TagSettingHeader(
                            tagEditActive = tagEditActive,
                            tagDeleteActive = tagDeleteActive,
                            onTagEdit = {
                                selectTags.firstOrNull()?.let { tag ->
                                    tagModifierActivityLauncher.launchTagModifierActivity(
                                        activity = this@TagSettingActivity,
                                        tag = tag
                                    )
                                }
                            },
                            onTagDelete = {
                                tagDeleteDialogDirector =
                                    tagDeleteDialogDirector.copy(isShow = true)
                            },
                            onBack = ::finish
                        )

                        Spacer(modifier = Modifier.weight(0.1f))

                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            LinkyText(
                                text = String.format(
                                    stringResource(R.string.tag_setting_title),
                                    state.tagCount
                                ),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ColorFamilyGray900AndGray100
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinkyText(
                                text = stringResource(R.string.tag_setting_desc),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorFamilyGray600AndGray400
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LinkyButton(
                                modifier = Modifier.height(36.dp),
                                text = stringResource(R.string.tag_setting_add_button),
                                fontSize = 13.sp,
                                onClick = { tagModifierActivityLauncher.launchTagModifierActivity(this@TagSettingActivity) }
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))

                        if (isLoading) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                LinearProgressIndicator()
                            }
                        } else if (showList) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ) {
                                items(
                                    count = tagWithLinkCounts.itemCount,
                                    key = tagWithLinkCounts.itemKey { it.tag.id ?: 0 },
                                    contentType = tagWithLinkCounts.itemContentType { "TagWithLinkCount" }
                                ) { index ->
                                    tagWithLinkCounts[index]?.let { tagWithLinkCount ->
                                        val tag = tagWithLinkCount.tag
                                        val isCheck by remember(selectTags) {
                                            derivedStateOf { selectTags.contains(tag) }
                                        }
                                        val rowModifier = if (isCheck) {
                                            Modifier.background(MainColor, CircleShape)
                                        } else {
                                            Modifier.border(1.dp, ColorFamilyGray600AndGray800, CircleShape)
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(60.dp)
                                                .background(MaterialTheme.colors.background)
                                                .clickableRipple(enableRipple = false) {
                                                    if (isCheck) {
                                                        selectTags -= tagWithLinkCount.tag
                                                    } else {
                                                        selectTags += tagWithLinkCount.tag
                                                    }
                                                },
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(Gray300)
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f)
                                                    .padding(horizontal = 20.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .then(rowModifier)
                                                        .size(24.dp)
                                                        .padding(4.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (isCheck) {
                                                        Icon(
                                                            imageVector = Icons.Filled.Check,
                                                            contentDescription = "check",
                                                            tint = Color.White,
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                LinkyText(
                                                    text = tagWithLinkCount.tag.name,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 16.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = ColorFamilyGray900AndGray100
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                LinkyText(
                                                    text = "${tagWithLinkCount.linkCount}",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp,
                                                    color = SubColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
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

@Composable
internal fun TagSettingHeader(
    tagEditActive: Boolean,
    tagDeleteActive: Boolean,
    onTagEdit: () -> Unit,
    onTagDelete: () -> Unit,
    onBack: () -> Unit,
) {
    val tagEditPainter by remember(tagEditActive) {
        derivedStateOf {
            if (tagEditActive) {
                R.drawable.icon_tag_edit_enable
            } else {
                R.drawable.icon_tag_edit_disable
            }
        }
    }
    val tagDeletePainter by remember(tagDeleteActive) {
        derivedStateOf {
            if (tagDeleteActive) {
                R.drawable.icon_tag_delete_enable
            } else {
                R.drawable.icon_tag_delete_disable
            }
        }
    }
    val tagEditModifier by remember(tagEditActive) {
        derivedStateOf {
            if (tagEditActive) {
                Modifier.throttleClickRipple(radius = 16.dp, onClick = onTagEdit)
            } else {
                Modifier
            }
        }
    }
    val tagDeleteModifier by remember(tagDeleteActive) {
        derivedStateOf {
            if (tagDeleteActive) {
                Modifier.throttleClickRipple(radius = 16.dp, onClick = onTagDelete)
            } else {
                Modifier
            }
        }
    }

    LinkyHeader {
        LinkyBackArrowButton(onClick = onBack)
        LinkyText(
            text = stringResource(R.string.tag_setting),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = ColorFamilyGray900AndGray100,
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = tagEditModifier,
            painter = painterResource(tagEditPainter),
            contentDescription = "tag_edit"
        )

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            modifier = tagDeleteModifier,
            painter = painterResource(tagDeletePainter),
            contentDescription = "tag_edit"
        )

        Spacer(modifier = Modifier.width(12.dp))
    }
}