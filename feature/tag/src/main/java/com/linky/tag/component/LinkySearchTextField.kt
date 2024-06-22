package com.linky.tag.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.LinkyBasicTextField
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.NoRippleTheme
import com.linky.design_system.util.clickableRipple

@Composable
internal fun LinkySearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onFocusChanged: (FocusState) -> Unit = {},
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions = KeyboardActions(
        onDone = { focusManager.clearFocus() }
    ),
) {
    NoRippleTheme {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.CenterStart,
        ) {
            LinkyBasicTextField(
                value = value,
                onValueChange = onValueChange,
                focusRequester = focusRequester,
                onFocusChanged = onFocusChanged,
                keyboardActions = keyboardActions,
                placeholder = {
                    LinkyText(
                        text = placeholder,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorFamilyGray600AndGray400
                    )
                },
                startContent = {
                    Image(
                        painter = painterResource(R.drawable.icon_tag_search),
                        contentDescription = "search"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                },
                endContent = {
                    Image(
                        painter = painterResource(R.drawable.icon_clear),
                        contentDescription = "clear",
                        modifier = Modifier.clickableRipple(radius = 14.dp, onClick = onClear)
                    )
                }
            )
        }
    }
}