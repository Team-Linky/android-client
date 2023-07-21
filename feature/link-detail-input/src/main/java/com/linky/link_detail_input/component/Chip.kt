package com.linky.link_detail_input.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.LinkyInputChipBackgroundColor
import com.linky.design_system.ui.theme.LinkyInputChipSelectTextColor
import com.linky.design_system.ui.theme.LinkyInputChipUnSelectTextColor
import com.linky.design_system.ui.theme.NoRippleTheme
import com.linky.design_system.ui.theme.ShadowBlue
import com.linky.link_detail_input.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun LinkyTagChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val backgroundColor = if (isSelected) LinkyInputChipBackgroundColor else ShadowBlue
    val textColor = if (isSelected) LinkyInputChipSelectTextColor else LinkyInputChipUnSelectTextColor
    val deleteIconRes = if (isSelected) R.drawable.ico_delete_tag_select else R.drawable.ico_delete_tag_unselect

    NoRippleTheme {
        FilterChip(
            selected = isSelected,
            shape = RoundedCornerShape(10.dp),
            onClick = onClick,
            colors = ChipDefaults.filterChipColors(
                backgroundColor = backgroundColor
            ),
            modifier = Modifier.height(29.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkyText(
                    text = text,
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.padding(start = 2.5.dp))
                Image(
                    painter = painterResource(deleteIconRes),
                    contentDescription = "delete",
                    modifier = Modifier.clickable { onDelete.invoke() }
                )
            }
        }
    }
}