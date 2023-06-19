package com.linky.design_system.ui.component.textfield

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.Gray600
import com.linky.design_system.ui.theme.LinkyBaseTextFieldCursorColor
import com.linky.design_system.ui.theme.LinkyTextDefaultColor
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.ui.theme.Pretendard

@Composable
fun LinkyBaseTextField(
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = MainColor,
        backgroundColor = LocalTextSelectionColors.current.backgroundColor,
    )
    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            maxLines = maxLines,
            textStyle = TextStyle(
                fontFamily = Pretendard,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LinkyTextDefaultColor,
                textDecoration = TextDecoration.None,
                lineHeight = 0.sp,
                letterSpacing = 0.sp,
                textAlign = TextAlign.Start
            ),
            cursorBrush = SolidColor(LinkyBaseTextFieldCursorColor),
            modifier = modifier,
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Password),
            keyboardActions = keyboardActions,
        )
    }

    if (value.isEmpty()) {
        LinkyText(
            text = placeholder,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Gray600
        )
    }
}