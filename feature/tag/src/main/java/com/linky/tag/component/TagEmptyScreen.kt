package com.linky.tag.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.R
import com.linky.design_system.ui.component.button.LinkyButton
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300

@Composable
internal fun TagEmptyScreen(
    modifier: Modifier = Modifier,
    onShowLinkActivity: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.4f))
        Image(
            painter = painterResource(R.drawable.image_tag_create),
            contentDescription = "link create"
        )
        LinkyText(
            text = stringResource(R.string.tag_create_description),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = ColorFamilyGray800AndGray300,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.weight(0.2f))
        LinkyButton(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.link_create_text),
            onClick = onShowLinkActivity
        )
        Spacer(modifier = Modifier.weight(0.4f))
    }
}

@Preview
@Composable
private fun TagEmptyScreenPreview() {
    Column {
        TagEmptyScreen(
            onShowLinkActivity = {}
        )
    }
}