package com.linky.lock

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.linky.pin_setting.extension.launchCRA
import com.linky.pin_setting.extension.rememberLauncherForPinSettingActivityResult
import com.linky.design_system.ui.component.more.LinkyDriver
import com.linky.design_system.ui.component.switch.LinkySwitchButton
import com.linky.design_system.ui.component.text.LinkyText
import com.linky.design_system.ui.theme.LockContentBackgroundColor
import com.linky.design_system.ui.theme.LockContentLineColor
import com.linky.design_system.util.clickableRipple
import com.linky.lock.component.LockHeader
import com.linky.lock.state.BiometricStatus
import com.linky.lock.state.LockStatus
import com.linky.navigation.more.MoreNavType
import org.orbitmvi.orbit.compose.collectAsState

fun NavGraphBuilder.lockScreen(onBack: () -> Unit) {
    composable(MoreNavType.Lock.route) { LinkyLockRoute(onBack = onBack) }
}

@Composable
private fun LinkyLockRoute(
    viewModel: LinkyLockViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.collectAsState()

    LinkyLockScreen(
        lockStatus = state.lockStatus,
        biometricStatus = state.biometricStatus,
        onBack = onBack,
        onChangeLock = { viewModel.doAction(Action.ToggleLock(it)) },
        onChangeBiometric = { viewModel.doAction(Action.ToggleBiometric(it)) }
    )
}

@Composable
private fun LinkyLockScreen(
    lockStatus: LockStatus,
    biometricStatus: BiometricStatus,
    onBack: () -> Unit = {},
    onChangeLock: (Boolean) -> Unit,
    onChangeBiometric: (Boolean) -> Unit,
) {
    val activity = LocalContext.current as ComponentActivity
    val canBiometric = rememberCanDeviceBiometric()

    var enableLock by remember { mutableStateOf(false) }
    var enableBiometric by remember { mutableStateOf(false) }

    LaunchedEffect(lockStatus) {
        if (lockStatus is LockStatus.Result) {
            enableLock = lockStatus.enable
        }
    }

    LaunchedEffect(biometricStatus) {
        if (biometricStatus is BiometricStatus.Result) {
            enableBiometric = biometricStatus.enable
        }
    }

    val craResult = rememberLauncherForPinSettingActivityResult(
        onSuccess = { enableLock = true },
        onFail = { enableLock = false },
        onComplete = { onChangeLock.invoke(enableLock) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LockHeader(onBack)
        Spacer(
            modifier = Modifier.padding(top = 16.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinkyText(
                    text = stringResource(R.string.lock_text),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Box(
                    modifier = Modifier.padding(end = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (lockStatus) {
                        is LockStatus.Loading -> {
                            CircularProgressIndicator()
                        }

                        is LockStatus.Result -> {
                            LinkySwitchButton(
                                modifier = Modifier.clickableRipple(radius = 10.dp) { craResult.launchCRA(activity, enableLock) },
                                checked = enableLock,
                            )
                        }
                    }
                }
            }
        }
        LinkyDriver()
        if (enableLock) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(LockContentBackgroundColor)
                    .clickable { }
            ) {
                LinkyText(
                    text = stringResource(R.string.lock_password_change_text),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
            if (canBiometric) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(LockContentLineColor)
                )
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

                    Box(
                        modifier = Modifier.padding(end = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        when (lockStatus) {
                            is LockStatus.Loading -> {
                                CircularProgressIndicator()
                            }

                            is LockStatus.Result -> {
                                LinkySwitchButton(
                                    checked = enableBiometric,
                                    onCheckedChange = { enableBiometric = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberCanDeviceBiometric(
    context: Context = LocalContext.current.applicationContext
): Boolean = remember {
    val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    val canAuthenticate = BiometricManager.from(context).canAuthenticate(authenticators)
    canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
}