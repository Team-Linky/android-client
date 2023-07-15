package com.linky.certification

import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.linky.certification.component.CertificationContent
import com.linky.design_system.ui.component.keypad.Keypad
import com.linky.intercation.vibrate.vibrateCompat
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun CertificationScreen(
    onClose: () -> Unit,
    viewModel: CertificationViewModel = hiltViewModel()
) {
    var password by remember { mutableStateOf("") }
    var isFail by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SideEffect.CertifiedFail -> {
                vibrator.vibrateCompat(
                    milliseconds = 400,
                    amplitude = 50
                )
                isFail = true
                password = ""
            }
            is SideEffect.CertifiedSuccess -> onClose.invoke()
        }
    }

    if (password.length == 4) {
        viewModel.certified(password)
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
        Spacer(modifier = Modifier.weight(0.1f))
        CertificationContent(
            title = stringResource(R.string.certification_title),
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