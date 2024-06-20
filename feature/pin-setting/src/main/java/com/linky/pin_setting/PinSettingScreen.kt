package com.linky.pin_setting

import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linky.design_system.R
import com.linky.design_system.ui.component.keypad.Keypad
import com.linky.intercation.vibrate.vibrateCompat
import com.linky.pin_setting.component.PinSettingContent
import com.linky.pin_setting.component.PinSettingHeader
import com.linky.pin_setting.state.PinSettingSideEffect
import com.linky.pin_setting.state.PinSettingStatus
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun PinSettingScreen(
    viewModel: PinSettingViewModel = viewModel(),
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val state by viewModel.collectAsState()
    var title by remember { mutableIntStateOf(R.string.pin_setting_title) }

    var pin by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)
    var isFail by remember { mutableStateOf(false) }

    LaunchedEffect(state.status) {
        when (state.status) {
            is PinSettingStatus.EnterPinScreen -> Unit

            is PinSettingStatus.ConfirmPinScreen -> {
                title = R.string.pin_setting_sub_title
            }
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PinSettingSideEffect.PinMismatch -> {
                vibrator.vibrateCompat(
                    milliseconds = 400,
                    amplitude = 50
                )
                isFail = true
                pin = ""
            }
            is PinSettingSideEffect.PinSaved -> {
                onComplete.invoke()
            }
        }
    }

    if (pin.length == 4 && state.status is PinSettingStatus.EnterPinScreen) {
        viewModel.doAction(Action.EnterPin(pin))
        pin = ""
    }

    if (pin.length == 4 && state.status is PinSettingStatus.ConfirmPinScreen) {
        viewModel.doAction(Action.ConfirmPin(pin))
    }

    if (isFail && pin.isNotEmpty()) {
        isFail = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PinSettingHeader(onBack)
        PinSettingContent(
            title = stringResource(state.titleRes),
            valueLength = pin.length,
            isFail = isFail
        )
        Keypad(
            onChangeValue = { value ->
                if (pin.length < 4) {
                    pin += value
                }
            },
            onDelete = {
                if (pin.isNotEmpty()) {
                    pin = pin.dropLast(1)
                }
            }
        )
    }
}