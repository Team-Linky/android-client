package com.linky.design_system.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.ui.theme.White

@Composable
fun LinkyButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    fontSize: TextUnit = 15.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = RoundedCornerShape(10.dp),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MainColor,
        disabledBackgroundColor = ColorFamilyGray400AndGray600,
    )
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        modifier = modifier
    ) {
        if (text != null) {
            LinkyText(
                text = text,
                color = White,
                fontSize = fontSize,
                fontWeight = fontWeight,
            )
        }
    }
}