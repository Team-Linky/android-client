package com.linky.pin

import android.os.Vibrator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.linky.common.biometric_compose.BiometricUseState
import com.linky.common.biometric_compose.LaunchedBiometric
import com.linky.common.biometric_compose.rememberAuthentication
import com.linky.common.biometric_compose.rememberCanDeviceBiometric
import com.linky.design_system.R
import com.linky.design_system.ui.component.keypad.Keypad
import com.linky.design_system.ui.component.password.Password
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.ColorFamilyGray800AndGray300
import com.linky.design_system.ui.theme.ErrorColor
import com.linky.intercation.vibrate.vibrateCompat
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun PinScreen(
    viewModel: PinViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    paddingValues: PaddingValues,
    onClose: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    var isFail by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val vibrator = context.getSystemService(Vibrator::class.java)
    val offsetX = remember { Animatable(0f) }

    val canBiometric = rememberCanDeviceBiometric()
    val showBiometricKeyPad by remember(viewModel.enableBiometric) {
        derivedStateOf { canBiometric && viewModel.enableBiometric }
    }
    var biometricUseState: BiometricUseState by remember { mutableStateOf(BiometricUseState.Idle) }

    val authBiometric = rememberAuthentication(
        onError = { biometricUseState = BiometricUseState.Idle },
        onFail = { biometricUseState = BiometricUseState.Idle },
        onSuccess = onClose
    )

    LaunchedBiometric(
        biometricUseState = biometricUseState,
        onUse = {
            launch(
                activity = activity,
                authBiometric = authBiometric,
                title = "생체 인증",
                subtitle = "생체 인증으로 화면 잠금을 해제합니다.",
                negativeButtonText = "생체인증 사용"
            )
        },
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is PinSideEffect.Finish -> {
                    onClose.invoke()
                }

                is PinSideEffect.PinMismatch -> {
                    vibrator.vibrateCompat(
                        milliseconds = 400,
                        amplitude = 50
                    )
                    isFail = true
                    pin = ""
                }
            }
        }
    }

    LaunchedEffect(showBiometricKeyPad) {
        if (showBiometricKeyPad) {
            biometricUseState = BiometricUseState.Use
        }
    }

    if (pin.length == 4) {
        viewModel.doAction(Action.PinCheck(pin))
    }

    if (isFail && pin.isNotEmpty()) {
        isFail = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        LaunchedEffect(isFail) {
            if (isFail) {
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = shakeKeyFrames
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.offset(offsetX.value.dp, 0.dp)
        ) {
            LinkyText(
                text = stringResource(R.string.pin_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (isFail) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.image_fail_password),
                        contentDescription = "fail"
                    )
                    LinkyText(
                        text = stringResource(R.string.pin_fail_description),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ErrorColor,
                    )
                }
            } else {
                LinkyText(
                    text = stringResource(R.string.pin_description),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorFamilyGray800AndGray300,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.padding(top = 35.dp))
            Password(pin.length)
        }

        Keypad(
            showBiometricKeyPad = showBiometricKeyPad,
            onChangeValue = { value ->
                if (pin.length < 4) {
                    pin += value
                }
            },
            onDelete = {
                if (pin.isNotEmpty()) {
                    pin = pin.dropLast(1)
                }
            },
            onBiometric = { biometricUseState = BiometricUseState.Use }
        )
    }
}

private val shakeKeyFrames: AnimationSpec<Float> = keyframes {
    durationMillis = 400
    val easing = FastOutLinearInEasing

    for (i in 1..8) {
        val x = when (i % 3) {
            0 -> 4f
            1 -> -4f
            else -> 0f
        }
        x at durationMillis / 10 * i with easing
    }
}