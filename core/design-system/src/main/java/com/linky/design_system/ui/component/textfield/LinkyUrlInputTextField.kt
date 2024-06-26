package com.linky.design_system.ui.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.NoRippleTheme
import com.linky.design_system.util.clickableRipple

@Composable
fun LinkyUrlInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onFocusChanged: (FocusState) -> Unit = {},
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions,
) {
    NoRippleTheme {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom
        ) {
            LinkyBasicTextField(
                modifier = Modifier
                    .height(34.dp)
                    .clickable { focusRequester.requestFocus() },
                value = value,
                onValueChange = onValueChange,
                focusRequester = focusRequester,
                onFocusChanged = onFocusChanged,
                keyboardActions = keyboardActions,
                placeholder = {
                    LinkyText(
                        text = placeholder,
                        fontSize = 14.dp,
                        fontWeight = FontWeight.Medium,
                        color = ColorFamilyGray600AndGray400
                    )
                },
                endContent = {
                    Image(
                        painter = painterResource(R.drawable.icon_clear),
                        contentDescription = "clear",
                        modifier = Modifier.clickableRipple(
                            radius = 14.dp,
                            onClick = onClear
                        )
                    )
                }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorFamilyGray900AndGray100)
            )
        }
    }
}