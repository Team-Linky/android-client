package com.linky.link_input_complete.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.button.LinkyButton
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.link_input_complete.R

@Composable
internal fun ColumnScope.InputCompleteContent(onCreate: () -> Unit) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        Image(
            painter = painterResource(R.drawable.image_create_complete),
            contentDescription = "complete"
        )
        LinkyText(
            text = stringResource(R.string.link_create_complete),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = ColorFamilyGray800AndGray300,
            modifier = Modifier.padding(top = 26.dp)
        )
        Spacer(modifier = Modifier.weight(0.2f))
        LinkyButton(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(46.dp),
            text = stringResource(R.string.link_create_text),
            onClick = onCreate
        )
        Spacer(modifier = Modifier.weight(0.5f))
    }
}