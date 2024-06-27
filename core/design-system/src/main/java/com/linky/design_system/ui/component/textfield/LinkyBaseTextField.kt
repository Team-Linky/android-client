package com.linky.design_system.ui.component.textfield

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.theme.ColorFamilyGray600AndGray400
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.ui.theme.Pretendard

@Composable
fun LinkyBaseTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    fontSize: Dp = 14.dp,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder: (@Composable () -> Unit)? = null
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = MainColor,
        backgroundColor = LocalTextSelectionColors.current.backgroundColor,
    )
    val fontSizeSp = with(LocalDensity.current) { fontSize.toSp() }

    Box {
        CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                maxLines = maxLines,
                textStyle = TextStyle(
                    fontFamily = Pretendard,
                    fontSize = fontSizeSp,
                    fontWeight = FontWeight.Medium,
                    color = ColorFamilyGray900AndGray100,
                    textDecoration = TextDecoration.None,
                    lineHeight = 0.sp,
                    letterSpacing = 0.sp,
                    textAlign = TextAlign.Start
                ),
                cursorBrush = SolidColor(ColorFamilyGray600AndGray400),
                modifier = modifier,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
            )
        }

        if (value.isEmpty()) {
            placeholder?.invoke()
        }
    }
}

@Composable
fun LinkyBaseTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    fontSize: Dp = 14.dp,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder: (@Composable () -> Unit)? = null
) {
    val textSelectionColors = TextSelectionColors(
        handleColor = MainColor,
        backgroundColor = LocalTextSelectionColors.current.backgroundColor,
    )
    val fontSizeSp = with(LocalDensity.current) { fontSize.toSp() }

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            maxLines = maxLines,
            textStyle = TextStyle(
                fontFamily = Pretendard,
                fontSize = fontSizeSp,
                fontWeight = FontWeight.Medium,
                color = ColorFamilyGray900AndGray100,
                textDecoration = TextDecoration.None,
                lineHeight = TextUnit.Unspecified,
                letterSpacing = TextUnit.Unspecified,
                textAlign = TextAlign.Start
            ),
            cursorBrush = SolidColor(ColorFamilyGray600AndGray400),
            modifier = modifier,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }

    if (value.text.isEmpty()) {
        placeholder?.invoke()
    }

}