package com.linky.link_input_complete.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linky.design_system.ui.component.button.LinkyCloseButton
import com.linky.design_system.ui.component.header.LinkyHeader

@Composable
internal fun InputCompleteHeader(onClose: () -> Unit) {
    LinkyHeader(
        modifier = Modifier.padding(end = 12.dp),
        horizontalArrangement = Arrangement.End
    ) {
        LinkyCloseButton(onClick = onClose)
    }
}