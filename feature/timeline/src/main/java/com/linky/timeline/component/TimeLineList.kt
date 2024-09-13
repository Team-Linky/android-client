package com.linky.timeline.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import com.linky.design_system.R
import com.linky.design_system.ui.component.more.TimeLineTagChip
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray300AndGray800
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray900
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray999
import com.linky.design_system.ui.theme.ErrorColor
import com.linky.design_system.ui.theme.Gray400
import com.linky.design_system.util.clickableRipple
import com.linky.model.Link
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TimeLineList(
    modifier: Modifier = Modifier,
    links: List<Link>,
    state: LazyListState,
    showAppending: Boolean,
    imageLoader: ImageLoader,
    onEdit: (Link) -> Unit,
    onRemove: (Long) -> Unit,
    onClick: (Link) -> Unit,
    onCopyLink: (Link) -> Unit,
) {
    val today = remember { LocalDate.now() }
    val yesterday = remember { today.minusDays(1) }
    val oneWeekAgo = remember { today.minusWeeks(1) }
    val oneMonthAgo = remember { today.minusMonths(1) }

    val grouping by remember(links) {
        derivedStateOf {
            links.groupBy { link ->
                val date = Instant
                    .ofEpochMilli(link.createAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                when {
                    date.isEqual(today) -> "오늘"
                    date.isEqual(yesterday) -> "어제"
                    date.isAfter(oneWeekAgo) -> "최근 일주일"
                    date.isAfter(oneMonthAgo) -> "최근 한 달"
                    else -> "오래된 데이터"
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier,
        state = state,
    ) {
        grouping.forEach { (groupName, groupItems) ->
            stickyHeader(
                key = groupName,
                contentType = "GroupName"
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.background),
                    contentAlignment = Alignment.CenterStart
                ) {
                    LinkyText(
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(start = 21.dp),
                        text = groupName,
                        color = ColorFamilyGray800AndGray400,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.dp
                    )
                }
            }
            items(
                items = groupItems,
                key = { it.id ?: 0L },
                contentType = { "TimeLineItems" }
            ) { link ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .padding(horizontal = 16.dp)
                        .clickableRipple(enableRipple = false) { onClick.invoke(link) },
                    shape = RoundedCornerShape(12.dp),
                    backgroundColor = ColorFamilyWhiteAndGray999
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
                                    text = link.createAtFormat,
                                    color = ColorFamilyGray800AndGray300,
                                    fontSize = 11.dp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .width(1.dp)
                                        .height(8.dp)
                                        .background(ColorFamilyGray600AndGray400)
                                )
                                LinkyText(
                                    text = link.readCountFormat,
                                    color = ColorFamilyGray800AndGray300,
                                    fontSize = 11.dp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            MenuButton(
                                key = link.id,
                                onEdit = { onEdit.invoke(link) },
                                onRemove = { onRemove.invoke(link.id!!) }
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .size(98.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                imageLoader = imageLoader,
                                model = link.openGraphData.image,
                                contentScale = ContentScale.Crop,
                                contentDescription = "thumbnail",
                                error = {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        painter = painterResource(R.drawable.image_default_link_thumbnail),
                                        contentDescription = "thumbnail"
                                    )
                                }
                            )
                            Column(
                                modifier = Modifier
                                    .height(98.dp)
                                    .padding(horizontal = 10.dp),
                            ) {
                                if (link.memo.isNotEmpty()) {
                                    LinkyText(
                                        text = link.memo,
                                        color = ColorFamilyGray900AndGray100,
                                        fontSize = 15.dp,
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 20.sp
                                    )
                                    Spacer(modifier = Modifier.padding(bottom = 7.dp))
                                }
                                LazyRow(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    items(link.tags) { tag ->
                                        TimeLineTagChip(
                                            modifier = Modifier.padding(end = 3.dp),
                                            tagName = tag.name,
                                        )
                                    }
                                }
                                Spacer(
                                    modifier = Modifier.weight(1f)
                                )
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
                                        text = link.openGraphData.title ?: "",
                                        color = ColorFamilyGray800AndGray300,
                                        fontSize = 12.dp,
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
                                            .clickableRipple(radius = 10.dp) {
                                                onCopyLink.invoke(link)
                                            },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showAppending) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun RowScope.MenuButton(
    key: Any?,
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
        key = key,
        modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(end = 10.dp),
        builder = builder,
        balloonContent = {
            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = ColorFamilyWhiteAndGray900,
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
                                onClick = {
                                    onEdit.invoke()
                                    balloonEvent = TimeLineMenuEvent.CLOSE
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LinkyText(
                            text = stringResource(R.string.link_menu_edit),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.dp,
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
                            .background(ColorFamilyGray300AndGray800)
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
                            text = stringResource(R.string.link_menu_delete),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.dp,
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
            var size by remember { mutableStateOf(IntSize.Zero) }
            var position by remember { mutableStateOf(Offset.Zero) }

            LaunchedEffect(balloonEvent) {
                when (balloonEvent) {
                    TimeLineMenuEvent.NONE -> Unit
                    TimeLineMenuEvent.CLOSE -> balloonWindow.dismiss()
                    TimeLineMenuEvent.OPEN -> balloonWindow.showAlignRight(
                        xOff = -(position.x * 0.55).toInt(),
                        yOff = size.height * 2
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.ima_sandwich_button),
                contentDescription = "more",
                modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        size = layoutCoordinates.size
                        position = layoutCoordinates.localToRoot(Offset.Zero)
                    }
                    .clickableRipple(radius = 10.dp) {
                        balloonEvent = TimeLineMenuEvent.OPEN
                    }
            )
        }
    )
}

internal enum class TimeLineMenuEvent {
    NONE, OPEN, CLOSE
}