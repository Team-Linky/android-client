package com.linky.link_url_input.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyNav300AndNav700
import com.linky.design_system.ui.theme.ColorFamilyNav700AndNav300
import com.linky.design_system.util.clickableRipple
import com.linky.link_url_input.R

@Composable
internal fun ClipBoardPaste(onClick: () -> Unit) {
    LinkyText(
        text = stringResource(R.string.link_clipboard_paste),
        fontSize = 12.sp,
        color = ColorFamilyNav700AndNav300,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .padding(top = 16.dp)
            .background(ColorFamilyNav300AndNav700, RoundedCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clickableRipple(radius = 100.dp, onClick = onClick)
    )
}