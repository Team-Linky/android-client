package com.linky.timeline.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ErrorColor
import com.linky.design_system.ui.theme.Gray400
import com.linky.design_system.ui.theme.LinkyDefaultBackgroundColor
import com.linky.design_system.ui.theme.LinkyDescriptionColor
import com.linky.design_system.ui.theme.LinkyTextDefaultColor
import com.linky.design_system.ui.theme.LinkyTimelineTextLineColor
import com.linky.design_system.ui.theme.LockContentLineColor
import com.linky.design_system.ui.theme.TimelineMenuBackgroundColor
import com.linky.design_system.util.clickableRipple
import com.linky.model.Link
import com.linky.timeline.R
import com.linky.timeline.TimeLineEmptyScreen
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor

@Composable
internal fun TimeLineContent(
    modifier: Modifier = Modifier,
    links: LazyPagingItems<Link>,
    onShowLinkActivity: () -> Unit,
    onClick: (Link) -> Unit,
    onEdit: (Long) -> Unit,
    onRemove: (Long) -> Unit,
    onCopyLink: (Link) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        when (links.loadState.refresh) {
            is LoadState.Loading -> {
                LinearProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is LoadState.NotLoading -> {
                if (links.itemCount > 0) {
                    TimeLineLinkScreen(
                        links = links,
                        onEdit = onEdit,
                        onRemove = onRemove,
                        onClick = onClick,
                        onCopyLink = onCopyLink,
                    )
                } else {
                    TimeLineEmptyScreen(onShowLinkActivity)
                }
            }

            is LoadState.Error -> {

            }
        }
    }
}

@Composable
internal fun TimeLineLinkScreen(
    links: LazyPagingItems<Link>,
    onEdit: (Long) -> Unit,
    onRemove: (Long) -> Unit,
    onClick: (Link) -> Unit,
    onCopyLink: (Link) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(links.itemCount) { index ->
            val link = links[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickableRipple(radius = 185.dp) { onClick.invoke(link!!) },
                shape = RoundedCornerShape(12.dp),
                backgroundColor = LinkyDefaultBackgroundColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 12.dp, start = 12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            LinkyText(
                                text = link?.createAtFormat ?: "",
                                color = LinkyDescriptionColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .width(1.dp)
                                    .height(8.dp)
                                    .background(LinkyTimelineTextLineColor)
                            )
                            LinkyText(
                                text = link?.readCountFormat ?: "",
                                color = LinkyDescriptionColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        MenuButton(
                            onEdit = { onEdit.invoke(link!!.id!!) },
                            onRemove = { onRemove.invoke(link!!.id!!) }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val context = LocalContext.current
                        val imageLoader = ImageLoader.Builder(context)
                            .memoryCache {
                                MemoryCache.Builder(context)
                                    .maxSizePercent(0.25)
                                    .build()
                            }
                            .diskCache {
                                DiskCache.Builder()
                                    .directory(context.cacheDir.resolve("linky_image_cache"))
                                    .maxSizePercent(0.02)
                                    .build()
                            }
                            .build()
                        AsyncImage(
                            modifier = Modifier
                                .size(98.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            imageLoader = imageLoader,
                            model = link?.openGraphData?.image,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                        Column(
                            modifier = Modifier
                                .height(98.dp)
                                .padding(horizontal = 10.dp),
                        ) {
                            if (!link?.memo.isNullOrEmpty()) {
                                LinkyText(
                                    text = link?.memo ?: "",
                                    color = LinkyTextDefaultColor,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.padding(bottom = 7.dp))
                            }
                            LazyRow(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                items(link?.tagList ?: emptyList()) { tag ->
                                    TimeLineTagChip(
                                        modifier = Modifier.padding(end = 3.dp),
                                        tagName = tag.name,
                                        onClick = {
                                            Log.d("123123", "link: $link")
                                            Log.d("123123", "tag: $tag")
                                        }
                                    )
                                }
                                item {
                                    TimeLineTagAddButton {}
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            // 태그
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Gray400)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LinkyText(
                                    text = link?.openGraphData?.title ?: "",
                                    color = LinkyDescriptionColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(0.7f),
                                )
                                Spacer(modifier = Modifier.weight(0.2f))

                                Image(
                                    painter = painterResource(R.drawable.ico_tag_copy),
                                    contentDescription = "copy",
                                    modifier = Modifier
                                        .weight(0.1f)
                                        .clickableRipple(radius = 10.dp) { onCopyLink.invoke(link!!) },
                                )
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.padding(bottom = 90.dp)) }
    }
}

@Composable
private fun RowScope.MenuButton(
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    var balloonEvent by remember { mutableStateOf(TimeLineMenuEvent.NONE) }
    val builder = rememberBalloonBuilder {
        arrowSize = 0
        setPadding(4)
        setBackgroundColor(Color.Transparent)
        setOnBalloonDismissListener {
            balloonEvent = TimeLineMenuEvent.NONE
        }
    }
    Balloon(
        modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(end = 10.dp),
        builder = builder,
        balloonContent = {
            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = TimelineMenuBackgroundColor,
                elevation = 3.dp,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.size(170.dp, 96.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickableRipple(
                                radius = 100.dp,
                                onClick = onEdit
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LinkyText(
                            text = "수정하기",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.image_menu_edit),
                            contentDescription = "edit",
                            modifier = Modifier.padding(end = 14.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(LockContentLineColor)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickableRipple(
                                radius = 100.dp,
                                onClick = {
                                    onRemove.invoke()
                                    balloonEvent = TimeLineMenuEvent.CLOSE
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LinkyText(
                            text = "삭제하기",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = ErrorColor,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.image_menu_delete),
                            contentDescription = "edit",
                            modifier = Modifier.padding(end = 14.dp)
                        )
                    }
                }
            }
        },
        content = { balloonWindow ->
            LaunchedEffect(balloonEvent) {
                when (balloonEvent) {
                    TimeLineMenuEvent.NONE -> Unit
                    TimeLineMenuEvent.OPEN -> balloonWindow.showAlignLeft()
                    TimeLineMenuEvent.CLOSE -> balloonWindow.dismiss()
                }
            }
            Image(
                painter = painterResource(R.drawable.ima_sandwich_button),
                contentDescription = "more",
                modifier = Modifier.clickableRipple(radius = 10.dp) {
                    balloonEvent = TimeLineMenuEvent.OPEN
                }
            )
        }
    )
}

internal enum class TimeLineMenuEvent {
    NONE, OPEN, CLOSE
}