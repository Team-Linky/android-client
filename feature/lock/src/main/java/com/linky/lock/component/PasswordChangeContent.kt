package com.linky.lock.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.LockContentBackgroundColor
import com.linky.lock.R

@Composable
internal fun PasswordChangeContent(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(LockContentBackgroundColor)
            .clickable { onClick.invoke() }
    ) {
        LinkyText(
            text = stringResource(R.string.lock_password_change_text),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}