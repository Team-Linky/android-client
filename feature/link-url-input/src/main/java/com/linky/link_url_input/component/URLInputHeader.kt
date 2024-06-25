package com.linky.link_url_input.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.util.throttleClickRipple
import com.linky.link_url_input.R

@Composable
internal fun URLInputHeader(
    isNextActive: Boolean = false,
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val textColor = if (isNextActive) {
        MainColor
    } else {
        ColorFamilyGray400AndGray600
    }

    val modifier = remember(isNextActive) {
        if (isNextActive) {
            Modifier.throttleClickRipple(
                radius = 12.dp,
                onClick = onNext
            )
        } else {
            Modifier
        }
    }

    LinkyHeader(
        modifier = Modifier.padding(start = 12.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LinkyBackArrowButton(onClick = onBack)
        LinkyText(
            text = stringResource(R.string.complete),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = textColor,
            modifier = Modifier
                .padding(start = 6.dp)
                .then(modifier)
        )
    }
}