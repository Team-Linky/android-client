package com.linky.timeline.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.linky.design_system.R

@Composable
internal fun TimeLineTagAddButton(onClick: () -> Unit) {
    Image(
        painter = painterResource(R.drawable.ico_tag_add),
        contentDescription = "tag_add",
        modifier = Modifier.clickable { onClick.invoke() }
    )
}