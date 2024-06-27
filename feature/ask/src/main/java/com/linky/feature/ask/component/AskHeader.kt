package com.linky.feature.ask.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.linky.design_system.R
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.util.throttleClickRipple

@Composable
internal fun AskHeader(
    isActiveSend: Boolean,
    onBack: () -> Unit,
    onSend: () -> Unit,
) {
    val textColor = if (isActiveSend) {
        MainColor
    } else {
        ColorFamilyGray400AndGray600
    }

    val modifier = remember(isActiveSend) {
        if (isActiveSend) {
            Modifier.throttleClickRipple(
                radius = 12.dp,
                onClick = onSend
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
            text = stringResource(R.string.ask_title),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.dp,
            color = ColorFamilyGray900AndGray100,
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        LinkyText(
            text = stringResource(R.string.ask_send),
            fontWeight = FontWeight.Medium,
            fontSize = 14.dp,
            color = textColor,
            modifier = Modifier
                .padding(start = 6.dp)
                .then(modifier)
        )
    }
}