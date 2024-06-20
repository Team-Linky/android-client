package com.linky.design_system.ui.component.keypad

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.NoRippleTheme

@Composable
fun ColumnScope.Keypad(
    showBiometricKeyPad: Boolean = false,
    onChangeValue: (String) -> Unit,
    onDelete: () -> Unit,
    onBiometric: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        KeypadRow {
            Keypad(
                text = "1",
                onClick = { onChangeValue.invoke("1") }
            )
            Keypad(
                text = "2",
                onClick = { onChangeValue.invoke("2") }
            )
            Keypad(
                text = "3",
                onClick = { onChangeValue.invoke("3") }
            )
        }
        KeypadRow {
            Keypad(
                text = "4",
                onClick = { onChangeValue.invoke("4") }
            )
            Keypad(
                text = "5",
                onClick = { onChangeValue.invoke("5") }
            )
            Keypad(
                text = "6",
                onClick = { onChangeValue.invoke("6") }
            )
        }
        KeypadRow {
            Keypad(
                text = "7",
                onClick = { onChangeValue.invoke("7") }
            )
            Keypad(
                text = "8",
                onClick = { onChangeValue.invoke("8") }
            )
            Keypad(
                text = "9",
                onClick = { onChangeValue.invoke("9") }
            )
        }
        KeypadRow {
            if (showBiometricKeyPad) {
                Keypad(
                    painter = painterResource(R.drawable.image_biometrics),
                    onClick = onBiometric
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .weight(3.33f)
                        .height(80.dp)
                )
            }
            Keypad(
                text = "0",
                onClick = { onChangeValue.invoke("0") }
            )
            Keypad(
                painter = painterResource(R.drawable.image_delete),
                onClick = onDelete
            )
        }
    }
}

@Composable
private fun KeypadRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        content = content
    )
}

@Composable
private fun RowScope.Keypad(
    text: String,
    onClick: () -> Unit
) {
    NoRippleTheme {
        Box(
            modifier = Modifier
                .weight(3.33f)
                .height(80.dp)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            LinkyText(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RowScope.Keypad(
    painter: Painter,
    onClick: () -> Unit
) {
    NoRippleTheme {
        Box(
            modifier = Modifier
                .weight(3.33f)
                .height(80.dp)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painter,
                contentDescription = ""
            )
        }
    }
}