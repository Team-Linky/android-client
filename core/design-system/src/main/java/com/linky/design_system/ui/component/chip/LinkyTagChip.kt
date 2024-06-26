package com.linky.design_system.ui.component.chip

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
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray100AndGray999
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray400
import com.linky.design_system.ui.theme.NoRippleTheme
import com.linky.design_system.ui.theme.ShadowBlue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LinkyTagChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val backgroundColor = if (isSelected) {
        ColorFamilyGray800AndGray400
    } else {
        ShadowBlue
    }

    val textColor = if (isSelected) {
        ColorFamilyGray100AndGray999
    } else {
        ColorFamilyGray800AndGray300
    }

    val deleteIcon = if (isSelected) {
        R.drawable.ico_delete_tag_select
    } else {
        R.drawable.ico_delete_tag_unselect
    }.let { painterResource(it) }

    NoRippleTheme {
        FilterChip(
            modifier = Modifier.height(29.dp),
            selected = isSelected,
            shape = RoundedCornerShape(10.dp),
            onClick = onClick,
            colors = ChipDefaults.filterChipColors(
                backgroundColor = backgroundColor
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkyText(
                    text = text,
                    color = textColor,
                    fontSize = 14.dp,
                    fontWeight = FontWeight.Medium
                )
                onDelete?.also { callback ->
                    Spacer(modifier = Modifier.padding(start = 2.5.dp))
                    Image(
                        modifier = Modifier.clickable(onClick = callback),
                        painter = deleteIcon,
                        contentDescription = "delete",
                    )
                }
            }
        }
    }
}