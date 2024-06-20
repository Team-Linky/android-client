package com.linky.lock.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.LinkyTextDefaultColor
import com.linky.design_system.R

@Composable
internal fun LockHeader(onBack: () -> Unit) {
    LinkyHeader(modifier = Modifier.padding(start = 12.dp)) {
        LinkyBackArrowButton(onBack)
        LinkyText(
            text = stringResource(R.string.lock_header),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = LinkyTextDefaultColor,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}