package com.linky.feature.tag_setting.modifier

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.linky.design_system.R
import com.linky.design_system.animation.slideIn
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.component.button.LinkyBackArrowButton
import com.linky.design_system.ui.component.header.LinkyHeader
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.component.textfield.LinkyUrlInputTextField
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.design_system.ui.theme.ColorFamilyGray400AndGray600
import com.linky.design_system.ui.theme.ColorFamilyGray900AndGray100
import com.linky.design_system.ui.theme.Gray600
import com.linky.design_system.ui.theme.LinkyLinkTheme
import com.linky.design_system.ui.theme.MainColor
import com.linky.design_system.util.throttleClickRipple
import com.linky.model.Tag
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun rememberLaunchTagModifierActivityResult(
    onSuccess: (Intent?) -> Unit = {},
    onCancel: () -> Unit = {}
) = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    when (result.resultCode) {
        Activity.RESULT_OK -> onSuccess.invoke(result.data)
        Activity.RESULT_CANCELED -> onCancel.invoke()
    }
}

fun ManagedActivityResultLauncher<Intent, ActivityResult>.launchTagModifierActivity(
    activity: ComponentActivity,
    tag: Tag? = null,
) {
    Intent(activity, TagModifierActivity::class.java).apply {
        putExtra("tag", tag)
        launch(this)
        activity.slideIn()
    }
}

@AndroidEntryPoint
class TagModifierActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LinkyLinkTheme {
                val viewModel = hiltViewModel<TagModifierViewModel>()
                val focusManager = LocalFocusManager.current
                val focusRequester = remember { FocusRequester() }
                val mode by remember { mutableStateOf(viewModel.mode) }

                var tagName by rememberSaveable { mutableStateOf(viewModel.tag?.name ?: "") }

                val isActiveComplete by remember(tagName) {
                    derivedStateOf { tagName.isNotEmpty() }
                }

                val title by remember(mode) {
                    derivedStateOf {
                        when (mode) {
                            ModifierMode.Add -> R.string.tag_add
                            ModifierMode.Edit -> R.string.tag_edit
                        }
                    }
                }

                val showEditDescription by remember(mode) {
                    derivedStateOf { mode == ModifierMode.Edit }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .addFocusCleaner(focusManager),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TagModifierHeader(
                        isActiveComplete = isActiveComplete,
                        onBack = {
                            setResult(RESULT_CANCELED)
                            finish()
                        },
                        onComplete = {
                            val tag = when (mode) {
                                ModifierMode.Add -> Tag(name = tagName)
                                ModifierMode.Edit -> viewModel.tag!!.copy(name = tagName)
                            }

                            viewModel.insert(
                                tag = tag,
                                success = {
                                    val cmd = when (mode) {
                                        ModifierMode.Add -> "add"
                                        ModifierMode.Edit -> "edit"
                                    }
                                    val data = Intent().apply {
                                        putExtra("cmd", cmd)
                                        putExtra("tagName", tagName)
                                    }
                                    setResult(RESULT_OK, data)
                                    finish()
                                },
                                error = {
                                    /* TODO 혹시 모를 에러 대응 */
                                }
                            )
                        }
                    )

                    Spacer(modifier = Modifier.weight(0.1f))

                    LinkyText(
                        text = stringResource(title),
                        fontSize = 22.dp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorFamilyGray900AndGray100
                    )

                    if (showEditDescription) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinkyText(
                            text = stringResource(R.string.tag_modifier_desc),
                            fontSize = 13.dp,
                            fontWeight = FontWeight.Medium,
                            color = Gray600
                        )
                        Spacer(modifier = Modifier.height(38.dp))
                    } else {
                        Spacer(modifier = Modifier.height(64.dp))
                    }

                    LinkyUrlInputTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 42.dp),
                        value = tagName,
                        placeholder = stringResource(R.string.tag_add_placeholder),
                        onValueChange = { value ->
                            tagName = if (value.length > 8) {
                                value.substring(0, 8)
                            } else {
                                value
                            }
                        },
                        onClear = { tagName = "" },
                        focusRequester = focusRequester,
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    override fun onPause() {
        if (isFinishing) {
            slideOut()
        }
        super.onPause()
    }

}

@Composable
private fun TagModifierHeader(
    isActiveComplete: Boolean,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val textColor = if (isActiveComplete) {
        MainColor
    } else {
        ColorFamilyGray400AndGray600
    }

    val modifier = remember(isActiveComplete) {
        if (isActiveComplete) {
            Modifier.throttleClickRipple(
                radius = 12.dp,
                onClick = onComplete
            )
        } else {
            Modifier
        }
    }

    LinkyHeader(
        modifier = Modifier.padding(start = 12.dp, end = 16.dp)
    ) {
        LinkyBackArrowButton(onClick = onBack)
        Spacer(modifier = Modifier.weight(1f))
        LinkyText(
            text = stringResource(R.string.complete),
            fontWeight = FontWeight.Medium,
            fontSize = 14.dp,
            color = textColor,
            modifier = Modifier
                .padding(start = 6.dp)
                .then(modifier)
        )
    }
}