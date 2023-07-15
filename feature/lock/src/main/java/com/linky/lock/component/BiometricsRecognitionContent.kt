package com.linky.lock.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linky.design_system.ui.component.switch.LinkySwitchButton
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.LockContentBackgroundColor
import com.linky.lock.BiometricStatus
import com.linky.lock.R

@Composable
internal fun BiometricsRecognitionContent(
    value: Boolean,
    biometricStatus: BiometricStatus,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(LockContentBackgroundColor)
    ) {
        LinkyText(
            text = stringResource(R.string.lock_biometrics_recognition_text),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 20.dp)
        )
        LinkySwitchButton(
            modifier = Modifier.padding(end = 20.dp),
            checked = value,
            onCheckedChange = onCheckedChange,
            enabled = biometricStatus is BiometricStatus.Success
        )
    }
}