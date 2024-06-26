package com.linky.design_system.ui.component.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.ShadowGray

@Composable
fun TimeLineTagChip(
    modifier: Modifier = Modifier,
    tagName: String,
) {
    Box(
        modifier = modifier
            .height(17.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(ShadowGray),
        contentAlignment = Alignment.Center
    ) {
        LinkyText(
            text = tagName,
            color = ColorFamilyGray800AndGray300,
            fontSize = 11.dp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 11.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}