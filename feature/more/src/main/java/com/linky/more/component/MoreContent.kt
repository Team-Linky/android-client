package com.linky.more.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.LinkyDefaultBackgroundColor
import com.linky.design_system.ui.theme.LinkyMoreContentLineColor
import com.linky.design_system.util.clickableRipple
import com.linky.more.R
import com.linky.navigation.more.MoreNavType

@Composable
internal fun MoreContent(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = LinkyDefaultBackgroundColor,
        elevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MoreContentItem(
                iconResource = R.drawable.icon_content_notification,
                textResource = R.string.more_content_notification,
                onClick = { onClick.invoke(MoreNavType.Notification.route) }
            )
            MoreContentItemLine()
            MoreContentItem(
                iconResource = R.drawable.icon_content_tip,
                textResource = R.string.more_content_tip,
                onClick = { onClick.invoke(MoreNavType.Tip.route) }
            )
            MoreContentItemLine()
            MoreContentItem(
                iconResource = R.drawable.icon_content_lock,
                textResource = R.string.more_content_lock,
                onClick = { onClick.invoke(MoreNavType.Lock.route) }
            )
            MoreContentItemLine()
            MoreContentItem(
                iconResource = R.drawable.icon_content_tag,
                textResource = R.string.more_content_tag,
                onClick = { onClick.invoke(MoreNavType.Tag.route) }
            )
            MoreContentItemLine()
            MoreContentItem(
                iconResource = R.drawable.icon_content_link,
                textResource = R.string.more_content_link,
                onClick = { onClick.invoke(MoreNavType.Link.route) }
            )
        }
    }
}

@Composable
private fun MoreContentItem(
    @DrawableRes iconResource: Int,
    @StringRes textResource: Int,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickableRipple(
                radius = 1000.dp,
                onClick = onClick
            )
    ) {
        Image(
            painter = painterResource(iconResource),
            contentDescription = "icon",
            modifier = Modifier.padding(start = 12.dp)
        )
        LinkyText(
            text = stringResource(textResource),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun MoreContentItemLine() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LinkyMoreContentLineColor)
    )
}