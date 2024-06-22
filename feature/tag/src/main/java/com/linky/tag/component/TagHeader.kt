package com.linky.tag.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.util.clickableRipple

@Composable
internal fun TagHeader(
    expanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit
) {
    LinkyHeader(
        modifier = Modifier.padding(start = 20.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LinkyText(
            text = stringResource(R.string.tag_text),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = ColorFamilyGray900AndGray100
        )
        if (!expanded) {
            Image(
                modifier = Modifier
                    .clickableRipple(radius = 24.dp) { onChangeExpanded.invoke(true) },
                painter = painterResource(R.drawable.icon_search),
                contentDescription = "search"
            )
        }
    }
}