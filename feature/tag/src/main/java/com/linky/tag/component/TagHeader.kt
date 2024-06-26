package com.linky.tag.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray800
import com.linky.design_system.util.clickableRipple

@Composable
internal fun TagHeader(
    searchValue: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    focusManager: FocusManager,
    focusRequester: FocusRequester,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(start = 20.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(
                visible = !isFocused,
            ) {
                LinkyText(
                    text = stringResource(R.string.tag_text),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.dp,
                    color = ColorFamilyGray900AndGray100
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinkySearchTextField(
                    modifier = Modifier
                        .clickable(onClick = focusRequester::requestFocus)
                        .weight(1f)
                        .height(38.dp)
                        .background(ColorFamilyWhiteAndGray800, RoundedCornerShape(12.dp))
                        .padding(start = 6.dp, end = 10.dp),
                    value = searchValue,
                    placeholder = stringResource(R.string.tag_search_placeholder),
                    focusManager = focusManager,
                    focusRequester = focusRequester,
                    onValueChange = onValueChange,
                    onFocusChanged = { isFocused = it.isFocused },
                    onClear = onClear,
                )
                AnimatedVisibility(visible = isFocused) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(6.dp))

                        LinkyText(
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 6.dp)
                                .clickableRipple(enableRipple = false) {
                                    onClear.invoke()
                                    focusRequester.freeFocus()
                                    focusManager.clearFocus()
                                },
                            text = stringResource(R.string.cancel),
                            fontSize = 14.dp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorFamilyGray600AndGray400
                        )
                    }
                }
            }
        }
    }
}