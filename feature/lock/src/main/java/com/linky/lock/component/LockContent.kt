package com.linky.lock.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.linky.design_system.ui.component.switch.LinkySwitchButton
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.lock.R

@Composable
internal fun LockContent(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Spacer(modifier = Modifier.padding(top = 16.dp))
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LinkyText(
                text = stringResource(R.string.lock_text),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
            LinkySwitchButton(
                modifier = Modifier.padding(end = 20.dp),
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}