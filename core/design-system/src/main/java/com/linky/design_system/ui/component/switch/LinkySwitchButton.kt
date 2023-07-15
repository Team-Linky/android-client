package com.linky.design_system.ui.component.switch

import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.linky.design_system.ui.theme.LinkyDefaultTheme
import com.linky.design_system.ui.theme.LinkySwitchUnCheckTrackColor
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.ui.theme.White

@Composable
fun LinkySwitchButton(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MainColor,
            checkedTrackColor = MainColor,
            checkedTrackAlpha = 0.5f,
            uncheckedThumbColor = White,
            uncheckedTrackColor = LinkySwitchUnCheckTrackColor,
            uncheckedTrackAlpha = 1f,
        )
    )
}

@Preview
@Composable
private fun LinkySwitchButtonPreview() {
    LinkyDefaultTheme {
        LinkySwitchButton()
    }
}