package com.linky.feature.ask

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.R
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.LinkyBaseTextField
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.design_system.ui.theme.ColorFamilyGray100AndGray600
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.ColorFamilyWhiteAndGray900
import com.linky.feature.ask.component.AskHeader
import com.linky.feature.ask.component.AskMenuBox
import com.linky.feature.ask.component.enterTransition
import com.linky.feature.ask.component.exitTransition

const val ASK_ROUTE = "ask_route"

fun NavGraphBuilder.askScreen(
    scaffoldState: ScaffoldState,
    onBack: () -> Unit,
) {
    composable(
        route = ASK_ROUTE,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition }
    ) {
        AskScreen(
            scaffoldState = scaffoldState,
            onBack = onBack,
        )
    }
}

@Composable
private fun AskScreen(
    scaffoldState: ScaffoldState,
    onBack: () -> Unit,
) {
    var askContent by remember { mutableStateOf(TextFieldValue("")) }
    var askCategory by remember { mutableStateOf(AskCategory.None) }
    val focusRequester = remember { FocusRequester() }

    val isActiveSend by remember(askCategory, askContent) {
        derivedStateOf { askCategory != AskCategory.None && askContent.text.isNotEmpty() }
    }

    val focusManager = LocalFocusManager.current
    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFamilyWhiteAndGray900)
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AskHeader(
            isActiveSend = isActiveSend,
            onBack = onBack,
            onSend = {
                val subject = when (askCategory) {
                    AskCategory.None -> R.string.ask_category_menu_none
                    AskCategory.Error -> R.string.ask_category_menu_error
                    AskCategory.Feature -> R.string.ask_category_menu_feature
                    AskCategory.More -> R.string.ask_category_menu_more
                }.let { ContextCompat.getString(activity.applicationContext, it) }

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "plain/text"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("rlaalstjr4434@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, askContent.text.replace("\n", ""))
                }

                activity.startActivity(intent)
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 42.dp, vertical = 32.dp),
        ) {
            LinkyText(
                text = stringResource(R.string.ask_category),
                fontSize = 13.dp,
                fontWeight = FontWeight.Medium,
                color = ColorFamilyGray800AndGray300
            )

            Spacer(modifier = Modifier.height(8.dp))

            AskMenuBox(
                context = activity.applicationContext,
                modifier = Modifier.fillMaxWidth(),
                currentCategory = askCategory,
                onChangeCategory = { askCategory = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            LinkyText(
                text = stringResource(R.string.ask_content),
                fontSize = 13.dp,
                fontWeight = FontWeight.Medium,
                color = ColorFamilyGray800AndGray300
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorFamilyGray100AndGray600, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                LinkyBaseTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp)
                        .focusRequester(focusRequester),
                    value = askContent,
                    singleLine = false,
                    fontSize = 16.dp,
                    maxLines = Int.MAX_VALUE,
                    placeholder = {
                        LinkyText(
                            text = stringResource(R.string.ask_content_placeholder),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.dp,
                            color = ColorFamilyGray400AndGray600,
                        )
                    },
                    onValueChange = { value ->
                        askContent = if (value.text.length > 1000) {
                            value.copy(text = value.text.substring(0, 1000))
                        } else {
                            value
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Default,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions {
                        askContent = TextFieldValue(askContent.text + "\n", askContent.selection)
                    }
                )
            }
        }
    }
}