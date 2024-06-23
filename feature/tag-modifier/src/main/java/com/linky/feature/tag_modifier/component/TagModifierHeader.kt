package com.linky.feature.tag_modifier.component

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

@Composable
internal fun TagModifierHeader(
    isActive: Boolean = false,
    onComplete: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val textColor = if (isActive) {
        MainColor
    } else {
        ColorFamilyGray400AndGray600
    }

    LinkyHeader(modifier = Modifier.padding(start = 12.dp, end = 16.dp)) {
        LinkyBackArrowButton(onClick = onBack)
        Spacer(modifier = Modifier.weight(1f))
        LinkyText(
            text = stringResource(R.string.complete),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = textColor,
            modifier = Modifier
                .padding(7.dp)
                .clickableRipple(
                    radius = 12.dp,
                    onClick = onComplete
                )
        )
    }
}