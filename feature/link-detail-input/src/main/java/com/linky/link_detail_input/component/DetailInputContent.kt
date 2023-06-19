package com.linky.link_detail_input.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.LinkyBasicTextField
import com.linky.design_system.ui.theme.LinkyDescriptionColor
import com.linky.design_system.ui.theme.NoRippleTheme
import com.linky.design_system.ui.theme.SubColor
import com.linky.design_system.ui.theme.WebContentBackgroundColor
import com.linky.design_system.ui.theme.WebContentLineColor
import com.linky.design_system.ui.theme.WebContentTitleColor
import com.linky.link_detail_input.R

@Composable
internal fun ColumnScope.DetailInputContent(
    memoValue: String,
    memoOnValueChange: (String) -> Unit,
    memoOnClear: () -> Unit,
    memoOnFocusChanged: (FocusState) -> Unit = {},
    memoFocusRequester: FocusRequester,
    tagValue: String,
    tagOnValueChange: (String) -> Unit,
    tagOnClear: () -> Unit,
    tagOnFocusChanged: (FocusState) -> Unit = {},
    tagFocusRequester: FocusRequester,
    focusManager: FocusManager,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 32.dp))
            LinkyText(
                text = stringResource(R.string.link_memo),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LinkyDescriptionColor
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            LinkyTextField(
                value = memoValue,
                placeholder = stringResource(R.string.link_memo_placeholder),
                onValueChange = memoOnValueChange,
                onClear = memoOnClear,
                onFocusChanged = memoOnFocusChanged,
                focusRequester = memoFocusRequester,
                focusManager = focusManager,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(top = 32.dp))
            LinkyText(
                text = stringResource(R.string.tag_add),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = LinkyDescriptionColor
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            LinkyTextField(
                value = tagValue,
                placeholder = stringResource(R.string.tag_add_placeholder),
                onValueChange = tagOnValueChange,
                onClear = tagOnClear,
                onFocusChanged = tagOnFocusChanged,
                focusRequester = tagFocusRequester,
                focusManager = focusManager,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.padding(top = 36.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(WebContentLineColor)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(WebContentBackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                LinkyText(
                    text = "네이버 지도",
                    color = SubColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                LinkyText(
                    text = "나나방콕 상무점",
                    color = WebContentTitleColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            LinkyText(
                text = "https://www.url.zz",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = LinkyDescriptionColor,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun LinkyTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onFocusChanged: (FocusState) -> Unit = {},
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        LinkyBasicTextField(
            value = value,
            onValueChange = onValueChange,
            onClear = onClear,
            placeholder = placeholder,
            focusRequester = focusRequester,
            onFocusChanged = onFocusChanged,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = modifier.clickable { focusRequester.requestFocus() }
        )
    }
}