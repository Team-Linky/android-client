package com.linky.more.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.linky.design_system.R
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100

@Composable
internal fun MoreHeader() {
    LinkyHeader(
        modifier = Modifier.padding(start = 20.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LinkyText(
            text = stringResource(R.string.more_text),
            fontWeight = FontWeight.Bold,
            fontSize = 22.dp,
            color = ColorFamilyGray900AndGray100
        )
    }
}