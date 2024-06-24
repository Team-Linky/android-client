package com.linky.link_detail_input.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.util.clickableRipple
import com.linky.link_detail_input.state.Mode

@Composable
internal fun DetailInputHeader(
    mode: Mode,
    isNextActive: Boolean = false,
    onComplete: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val textColor = if (isNextActive) {
        MainColor
    } else {
        ColorFamilyGray400AndGray600
    }

    val titleRes = if (mode == Mode.Creator) {
        R.string.link_add
    } else {
        R.string.link_edit
    }

    LinkyHeader(modifier = Modifier.padding(start = 12.dp, end = 16.dp)) {
        LinkyBackArrowButton(onClick = onBack)
        LinkyText(
            text = stringResource(titleRes),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        LinkyText(
            text = stringResource(R.string.complete),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = textColor,
            modifier = Modifier
                .padding(start = 6.dp)
                .clickableRipple(
                    radius = 12.dp,
                    onClick = onComplete
                )
        )
    }
}