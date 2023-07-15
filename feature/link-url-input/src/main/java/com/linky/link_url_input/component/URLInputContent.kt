package com.linky.link_url_input.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.LinkyBasicTextField
import com.linky.design_system.ui.theme.NoRippleTheme
import com.linky.link_url_input.R

@Composable
internal fun ColumnScope.URLInputContent(
    value: String,
    hasClipBoard: Boolean,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onClipBoardPaste: () -> Unit,
    onFocusChanged: (FocusState) -> Unit = {},
    focusManager: FocusManager,
    focusRequester: FocusRequester,
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 58.dp))
        StableURLInputContent()
        Spacer(modifier = Modifier.padding(top = 56.dp))
        NoRippleTheme {
            LinkyBasicTextField(
                value = value,
                onValueChange = onValueChange,
                onClear = onClear,
                placeholder = stringResource(R.string.link_add_placeholder),
                focusRequester = focusRequester,
                onFocusChanged = onFocusChanged,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 42.dp)
                    .clickable { focusRequester.requestFocus() }
            )
        }
        if (value.isEmpty() && hasClipBoard) {
            ClipBoardPaste(onClick = onClipBoardPaste)
        }
    }
}

@Composable
fun StableURLInputContent() {
    LinkyText(
        text = stringResource(R.string.link_add),
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    )
}