package com.linky.design_system.ui.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.linky.design_system.R
import com.linky.design_system.ui.theme.LinkyTextDefaultColor
import com.linky.design_system.util.clickableRipple

@Composable
fun LinkyBasicTextField(
    value: String,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onClear: () -> Unit = {},
    focusRequester: FocusRequester,
    onFocusChanged: (FocusState) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.height(34.dp)
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(1f)
            ) {
                LinkyBaseTextField(
                    value = value,
                    placeholder = placeholder,
                    onValueChange = onValueChange,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged(onFocusChanged)
                )
            }

            if (value.isNotEmpty()) {
                Image(
                    painter = painterResource(R.drawable.icon_clear),
                    contentDescription = "clear",
                    modifier = Modifier.clickableRipple(
                        radius = 14.dp,
                        onClick = onClear
                    )
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LinkyTextDefaultColor)
        )
    }
}