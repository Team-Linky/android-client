package com.linky.timeline.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray400
import com.linky.timeline.state.Sort

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TimeLineHeader(
    sorts: List<Sort>,
    sortType: Sort,
    onChangeSort: (Sort) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    LinkyHeader(modifier = Modifier.padding(start = 20.dp, end = 16.dp)) {
        Image(
            painter = painterResource(R.drawable.icon_appbar_logo),
            contentDescription = "logo"
        )
        LinkyText(
            text = stringResource(R.string.app_name).uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            MenuText(sortType = sortType)

            ExposedDropdownMenu(
                modifier = Modifier.width(90.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sorts.forEach { sort ->
                    DropdownMenuItem(
                        enabled = sortType != sort,
                        onClick = {
                            onChangeSort.invoke(sort)
                            expanded = false
                        }
                    ) {
                        MenuText(sortType = sort)
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuText(
    sortType: Sort,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinkyText(
            text = sortType.text,
            color = ColorFamilyGray800AndGray400,
            maxLines = 1,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Image(
            painter = painterResource(R.drawable.icon_combo_arrow),
            contentDescription = "combo"
        )
    }
}