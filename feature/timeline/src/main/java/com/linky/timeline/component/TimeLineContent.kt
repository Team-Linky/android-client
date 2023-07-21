package com.linky.timeline.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.linky.design_system.ui.component.button.LinkyButton
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.Gray400
import com.linky.design_system.ui.theme.LinkyDefaultBackgroundColor
import com.linky.design_system.ui.theme.LinkyDescriptionColor
import com.linky.design_system.ui.theme.LinkyTextDefaultColor
import com.linky.design_system.ui.theme.LinkyTimelineTextLineColor
import com.linky.design_system.util.clickableRipple
import com.linky.model.Link
import com.linky.timeline.R

@Composable
internal fun ColumnScope.TimeLineContent(
    onShowLinkActivity: () -> Unit,
    onClick: (Link) -> Unit,
    links: LazyPagingItems<Link>
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (links.itemCount > 0) {
            TimeLineLinkScreen(links, onClick)
        } else {
            TimeLineEmptyScreen(onShowLinkActivity)
        }
    }
}

@Composable
private fun TimeLineLinkScreen(
    links: LazyPagingItems<Link>,
    onClick: (Link) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(links) { link ->
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
                        Image(
                            painter = painterResource(R.drawable.ima_sandwich_button),
                            contentDescription = "more",
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .clickableRipple(radius = 10.dp) { }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(98.dp)
                                .clip(RoundedCornerShape(4.dp)),
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
                                        .clickableRipple(radius = 10.dp) { },
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
private fun ColumnScope.TimeLineEmptyScreen(
    onShowLinkActivity: () -> Unit
) {
    Spacer(modifier = Modifier.weight(0.4f))
    Image(
        painter = painterResource(R.drawable.image_clock),
        contentDescription = "link create"
    )
    LinkyText(
        text = stringResource(R.string.link_create_description),
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        color = LinkyDescriptionColor,
        modifier = Modifier.padding(top = 16.dp),
    )
    Spacer(modifier = Modifier.weight(0.2f))
    LinkyButton(
        modifier = Modifier.padding(horizontal = 24.dp),
        text = stringResource(R.string.link_create_text),
        onClick = onShowLinkActivity
    )
    Spacer(modifier = Modifier.weight(0.4f))
}