package com.linky.pin_setting.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.pin_setting.R
import com.linky.design_system.ui.component.password.Password
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.CertificationDescriptionColor
import com.linky.design_system.ui.theme.ErrorColor

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
internal fun PinSettingContent(
    title: String,
    valueLength: Int,
    isFail: Boolean
) {
    val offsetX = remember { Animatable(0f) }

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
            text = title,
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
        Password(valueLength)
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