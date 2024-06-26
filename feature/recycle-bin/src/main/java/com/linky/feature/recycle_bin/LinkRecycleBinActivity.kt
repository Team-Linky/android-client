package com.linky.feature.recycle_bin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import com.linky.design_system.R
import com.linky.design_system.animation.slideIn
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.button.LinkyButton
import com.linky.design_system.ui.component.check.LinkyCheck
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray300AndGray800
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray100
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray999
import com.linky.design_system.ui.theme.ErrorColor
import com.linky.design_system.ui.theme.LinkyLinkTheme
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.util.clickableRipple
import com.linky.design_system.util.rememberImageLoader
import com.linky.design_system.util.throttleClickRipple
import com.linky.feature.recycle_bin.component.DeleteLinkConfirmDialog
import com.linky.feature.recycle_bin.component.LinkDeleteDialogDirector
import com.linky.model.Link
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState

fun ComponentActivity.launchRecycleBinActivity() {
    Intent(this, LinkRecycleBinActivity::class.java).apply {
        startActivity(this)
        slideIn()
    }
}

@AndroidEntryPoint
class LinkRecycleBinActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinkyLinkTheme {
                val viewModel = hiltViewModel<LinkRecycleBinViewModel>()
                val state by viewModel.collectAsState()
                val scaffoldState = rememberScaffoldState()
                val imageLoader = rememberImageLoader()
                val links = state.links.collectAsLazyPagingItems()

                val selectLinks = remember { mutableStateListOf<Link>() }

                val isNotEmptyList by remember(state.linksCount) {
                    derivedStateOf { state.linksCount > 0 }
                }

                val isActiveButtons by remember(selectLinks) {
                    derivedStateOf { selectLinks.size > 0 }
                }

                var isAllCheckList by rememberSaveable { mutableStateOf(false) }

                val isLoading by remember(links.loadState) {
                    derivedStateOf { links.loadState.refresh is LoadState.Loading }
                }

                val showList by remember(links.loadState) {
                    derivedStateOf {
                        links.loadState.refresh is LoadState.NotLoading &&
                                links.itemSnapshotList.isNotEmpty()
                    }
                }

                var linkDeleteDialogDirector by remember {
                    mutableStateOf(LinkDeleteDialogDirector.Init)
                }

                LaunchedEffect(selectLinks, state.linksCount) {
                    isAllCheckList = selectLinks.size == state.linksCount && state.linksCount > 0
                }

                DeleteLinkConfirmDialog(
                    director = linkDeleteDialogDirector,
                    onCancel = { linkDeleteDialogDirector = LinkDeleteDialogDirector.Init },
                    onDelete = {
                        if (isAllCheckList) {
                            viewModel.doAction(RecycleBinAction.ClearAll)
                        } else {
                            viewModel.doAction(RecycleBinAction.DeleteLinks(selectLinks))
                        }
                        linkDeleteDialogDirector = LinkDeleteDialogDirector.Init
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
                        LinkRecycleBinHeader(
                            isNotEmptyList = isNotEmptyList,
                            isAllCheckList = isAllCheckList,
                            onBack = ::finish,
                            onAllCheck = {
                                isAllCheckList = !isAllCheckList

                                if (!isAllCheckList) {
                                    selectLinks.clear()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.weight(0.1f))

                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            LinkyText(
                                text = String.format(stringResource(R.string.recycle_bin_title), state.linksCount),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ColorFamilyGray900AndGray100
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row {
                                LinkyButton(
                                    modifier = Modifier.height(36.dp),
                                    text = stringResource(R.string.recycle_bin_btn_name_recycle),
                                    enabled = isActiveButtons,
                                    fontSize = 13.sp,
                                    onClick = { viewModel.doAction(RecycleBinAction.RecycleLinks(selectLinks)) },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = ErrorColor,
                                        disabledBackgroundColor = ColorFamilyGray400AndGray600,
                                    ),
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                LinkyButton(
                                    modifier = Modifier.height(36.dp),
                                    text = stringResource(R.string.recycle_bin_btn_name_clear),
                                    enabled = isActiveButtons,
                                    fontSize = 13.sp,
                                    onClick = {
                                        linkDeleteDialogDirector = linkDeleteDialogDirector.copy(isShow = true)
                                    },
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        if (isLoading) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                LinearProgressIndicator()
                            }
                        } else if (showList) {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(
                                    count = links.itemCount,
                                    key = links.itemKey { it.id ?: 0 },
                                    contentType = links.itemContentType { "LinkContentType" }
                                ) { index ->
                                    links[index]?.let { link ->
                                        val isChecked by remember(selectLinks) {
                                            derivedStateOf { selectLinks.contains(link) }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(80.dp)
                                                .clickableRipple(enableRipple = false) {
                                                    if (isChecked) {
                                                        selectLinks -= link
                                                    } else {
                                                        selectLinks += link
                                                    }
                                                },
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(ColorFamilyGray300AndGray800)
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f)
                                                    .padding(vertical = 12.dp, horizontal = 20.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                SubcomposeAsyncImage(
                                                    modifier = Modifier
                                                        .size(56.dp)
                                                        .clip(RoundedCornerShape(8.dp)),
                                                    imageLoader = imageLoader,
                                                    model = link.openGraphData.image,
                                                    contentScale = ContentScale.Crop,
                                                    contentDescription = "thumbnail",
                                                    loading = {
                                                        Box(
                                                            modifier = Modifier.size(56.dp),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            CircularProgressIndicator()
                                                        }
                                                    },
                                                    error = {
                                                        Image(
                                                            modifier = Modifier.size(56.dp),
                                                            painter = painterResource(R.drawable.image_default_link_thumbnail),
                                                            contentDescription = "thumbnail"
                                                        )
                                                    }
                                                )
                                                Column(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .padding(start = 10.dp, end = 28.dp),
                                                ) {
                                                    LinkyText(
                                                        text = link.openGraphData.title ?: link.memo,
                                                        color = ColorFamilyGray900AndGray100,
                                                        fontSize = 15.sp,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        fontWeight = FontWeight.SemiBold
                                                    )

                                                    Spacer(modifier = Modifier.height(2.dp))

                                                    LinkyText(
                                                        text = link.openGraphData.url!!,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = ColorFamilyGray800AndGray300,
                                                    )

                                                    Spacer(modifier = Modifier.height(2.dp))

                                                    Row(
                                                        modifier = Modifier.weight(1f),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        LinkyText(
                                                            text = link.createAtFormat,
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = ColorFamilyGray600AndGray400,
                                                        )
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxHeight()
                                                                .width(1.dp)
                                                                .background(
                                                                    ColorFamilyGray400AndGray600
                                                                )
                                                                .padding(horizontal = 4.dp)
                                                        )
                                                        LinkyText(
                                                            text = link.readCountFormat,
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = ColorFamilyGray600AndGray400,
                                                        )
                                                    }
                                                }

                                                LinkyCheck(
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .padding(4.dp),
                                                    checked = isAllCheckList || isChecked,
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
private fun LinkRecycleBinHeader(
    isNotEmptyList: Boolean,
    isAllCheckList: Boolean,
    onBack: () -> Unit,
    onAllCheck: () -> Unit,
) {
    val color = if (isAllCheckList) {
        MainColor
    } else if (isNotEmptyList) {
        ColorFamilyGray800AndGray100
    } else {
        ColorFamilyGray400AndGray600
    }

    val modifier = remember(isNotEmptyList) {
        if (isNotEmptyList) {
            Modifier.throttleClickRipple(
                enableRipple = false,
                radius = 12.dp,
                onClick = onAllCheck
            )
        } else {
            Modifier
        }
    }

    LinkyHeader(
        modifier = Modifier.padding(start = 12.dp, end = 16.dp)
    ) {
        LinkyBackArrowButton(onClick = onBack)
        LinkyText(
            text = stringResource(R.string.recycle_bin_header),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = ColorFamilyGray900AndGray100,
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .padding(8.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Filled.Check,
                contentDescription = "check",
                tint = color,
            )
            Spacer(modifier = Modifier.width(3.dp))
            LinkyText(
                text = stringResource(R.string.select_all),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color,
            )
        }
    }
}