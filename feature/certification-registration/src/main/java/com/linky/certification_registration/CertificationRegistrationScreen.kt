package com.linky.certification_registration

import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.linky.certification_registration.component.CertificationContent
import com.linky.certification_registration.component.CertificationRegistrationHeader
import com.linky.design_system.ui.component.keypad.Keypad
import com.linky.intercation.vibrate.vibrateCompat
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun CertificationRegistrationScreen(
    viewModel: CertificationRegistrationViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val state = viewModel.collectAsState().value
    var title by remember { mutableIntStateOf(R.string.certification_title) }

    var password by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)
    var isFail by remember { mutableStateOf(false) }

    when (state) {
        is State.FirstInputScreen -> {

        }

        is State.SecondInputScreen -> {
            title = R.string.certification_sub_title
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SideEffect.RegisterSuccess -> onComplete.invoke()
            SideEffect.RegisterFail -> {
                vibrator.vibrateCompat(
                    milliseconds = 400,
                    amplitude = 50
                )
                isFail = true
                password = ""
            }
        }
    }

    if (password.length == 4 && state is State.FirstInputScreen) {
        viewModel.setFirstPassword(password)
        password = ""
    }

    if (password.length == 4 && state is State.SecondInputScreen) {
        viewModel.setPassword(password)
    }

    if (isFail && password.isNotEmpty()) {
        isFail = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CertificationRegistrationHeader(onBack)
        CertificationContent(
            title = if (viewModel.tempPasswordIsNotEmpty) stringResource(R.string.certification_sub_title) else stringResource(
                R.string.certification_title
            ),
            valueLength = password.length,
            isFail = isFail
        )
        Keypad(
            onChangeValue = { value ->
                if (password.length < 4) {
                    password += value
                }
            },
            onDelete = {
                if (password.isNotEmpty()) {
                    password = password.dropLast(1)
                }
            }
        )
    }
}