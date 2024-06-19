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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.linky.design_system.ui.component.keypad.Keypad
import com.linky.design_system.ui.component.password.Password
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.CertificationDescriptionColor
import com.linky.design_system.ui.theme.ErrorColor
import com.linky.intercation.vibrate.vibrateCompat
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun PinScreen(
    viewModel: PinViewModel = hiltViewModel(),
    onClose: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    var isFail by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)
    val offsetX = remember { Animatable(0f) }

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

    if (pin.length == 4) {
        viewModel.doAction(Action.PinCheck(pin))
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
                text = stringResource(R.string.certification_title),
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
                        text = stringResource(R.string.certification_fail_description),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ErrorColor,
                    )
                }
            } else {
                LinkyText(
                    text = stringResource(R.string.certification_description),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CertificationDescriptionColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.padding(top = 35.dp))
            Password(pin.length)
        }

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