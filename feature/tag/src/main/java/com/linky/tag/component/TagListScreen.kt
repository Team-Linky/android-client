package com.linky.tag.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.RandomColor
import com.linky.design_system.ui.theme.ShadowBlack
import com.linky.design_system.ui.theme.White
import com.linky.design_system.util.clickableRipple
import com.linky.model.Tag

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TagListScreen(
    tagList: List<Tag>,
    onClick: (Tag) -> Unit
) {
    LazyColumn {
        item {
            FlowRow {
                tagList.forEach { tag ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(bottom = 10.dp, start = 4.dp, end = 4.dp)
                            .size(160.dp, 110.dp)
                            .background(RandomColor, RoundedCornerShape(12.dp))
                            .clickableRipple(radius = 80.dp) { onClick.invoke(tag) }
                    ) {
                        LinkyText(
                            text = tag.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = White,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = ShadowBlack,
                                    offset = Offset(0f, 0f),
                                    blurRadius = 10f
                                )
                            )
                        )
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.padding(bottom = 90.dp)) }
    }
}