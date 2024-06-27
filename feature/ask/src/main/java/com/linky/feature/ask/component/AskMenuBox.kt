package com.linky.feature.ask.component

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.feature.ask.AskCategory
import com.linky.feature.ask.askCategories

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AskMenuBox(
    context: Context = LocalContext.current,
    modifier: Modifier = Modifier,
    currentCategory: AskCategory,
    onChangeCategory: (AskCategory) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val currentMenuTextColor = if (currentCategory == AskCategory.None) {
        ColorFamilyGray400AndGray600
    } else {
        ColorFamilyGray900AndGray100
    }

    val subject by remember(currentCategory) {
        derivedStateOf {
            when (currentCategory) {
                AskCategory.None -> R.string.ask_category_menu_none
                AskCategory.Error -> R.string.ask_category_menu_error
                AskCategory.Feature -> R.string.ask_category_menu_feature
                AskCategory.More -> R.string.ask_category_menu_more
            }.let { ContextCompat.getString(context, it) }
        }
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkyText(
                    text = subject,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.dp,
                    color = currentMenuTextColor
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                TrailingIcon(
                    expanded = expanded
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorFamilyGray900AndGray100)
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            askCategories.forEach { category ->
                DropdownMenuItem(
                    enabled = category != currentCategory,
                    onClick = {
                        onChangeCategory.invoke(category)
                        expanded = false
                    }
                ) {
                    val text = when (category) {
                        AskCategory.None -> R.string.ask_category_menu_none
                        AskCategory.Error -> R.string.ask_category_menu_error
                        AskCategory.Feature -> R.string.ask_category_menu_feature
                        AskCategory.More -> R.string.ask_category_menu_more
                    }.let { stringResource(it) }

                    LinkyText(text = text)
                }
            }
        }
    }
}