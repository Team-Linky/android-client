package com.linky.link_url_input.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.LinkInputCompleteTextButtonDisableColor
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.util.clickableRipple
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
        LinkInputCompleteTextButtonDisableColor
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(start = 12.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
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
                .clickableRipple(
                    radius = 12.dp,
                    onClick = onNext
                )
        )
    }
}