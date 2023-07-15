package com.linky.timeline.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.timeline.R

@Composable
internal fun TimeLineHeader() {
    LinkyHeader(modifier = Modifier.padding(start = 20.dp, end = 16.dp)) {
        Image(
            painter = painterResource(R.drawable.icon_appbar_logo),
            contentDescription = "logo"
        )
        LinkyText(
            text = stringResource(R.string.app_name).uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}