package com.linky.lock

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.linky.common.biometric_compose.BiometricUseState
import com.linky.common.biometric_compose.LaunchedBiometric
import com.linky.common.biometric_compose.rememberAuthentication
import com.linky.common.biometric_compose.rememberCanDeviceBiometric
import com.linky.design_system.R
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
import com.linky.pin.extension.launchPinActivity
import com.linky.pin.extension.rememberLaunchPinActivityResult
import com.linky.pin_setting.extension.launchCRA
import com.linky.pin_setting.extension.rememberLauncherForPinSettingActivityResult
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
    val activity = LocalContext.current as FragmentActivity
    val canBiometric = rememberCanDeviceBiometric()

    var enableLock by remember { mutableStateOf(false) }
    var enableBiometric by remember { mutableStateOf(false) }

    var pinLockUseState: PinLockUseState by remember { mutableStateOf(PinLockUseState.Idle) }
    var biometricUseState: BiometricUseState by remember { mutableStateOf(BiometricUseState.Idle) }

    val pinLauncherSuccessHandler: () -> Unit = {
        if (pinLockUseState == PinLockUseState.UnLock) {
            enableLock = false
            onChangeLock.invoke(enableLock)
        }
    }

    val biometricLauncherSuccessHandler: () -> Unit = {
        when (biometricUseState) {
            is BiometricUseState.DoNotUse -> {
                enableBiometric = false
                onChangeBiometric.invoke(enableBiometric)
            }

            is BiometricUseState.Use -> {
                enableBiometric = true
                onChangeBiometric.invoke(enableBiometric)
            }

            is BiometricUseState.Idle -> Unit
        }
    }

    val authBiometric = rememberAuthentication(
        onError = { biometricUseState = BiometricUseState.Idle },
        onFail = { biometricUseState = BiometricUseState.Idle },
        onSuccess = biometricLauncherSuccessHandler
    )

    LaunchedBiometric(
        biometricUseState = biometricUseState,
        onUse = {
            launch(
                activity = activity,
                authBiometric = authBiometric,
                title = ContextCompat.getString(activity, R.string.biometric_default_title),
                subtitle = ContextCompat.getString(activity, R.string.biometric_use_subtitle),
                negativeButtonText = ContextCompat.getString(activity, R.string.biometric_use_text)
            )
        },
        onDoNotUse = {
            launch(
                activity = activity,
                authBiometric = authBiometric,
                title = ContextCompat.getString(activity, R.string.biometric_default_title),
                subtitle = ContextCompat.getString(activity, R.string.biometric_do_not_use_subtitle),
                negativeButtonText = ContextCompat.getString(activity, R.string.biometric_do_not_use_text)
            )
        }
    )

    val craLauncher = rememberLauncherForPinSettingActivityResult { isSuccess ->
        enableLock = isSuccess
        pinLockUseState = PinLockUseState.Idle
        onChangeLock.invoke(isSuccess)
    }

    val pinLauncher = rememberLaunchPinActivityResult(
        onCancel = { },
        onSuccess = pinLauncherSuccessHandler,
    )

    LaunchedEffect(pinLockUseState) {
        when (pinLockUseState) {
            is PinLockUseState.Idle -> Unit
            is PinLockUseState.Lock -> craLauncher.launchCRA(activity)
            is PinLockUseState.UnLock -> pinLauncher.launchPinActivity(activity)
        }
    }

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
                                modifier = Modifier
                                    .clickableRipple(radius = 10.dp) {
                                        pinLockUseState = if (enableLock) {
                                            PinLockUseState.UnLock
                                        } else {
                                            PinLockUseState.Lock
                                        }
                                    },
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
                        text = stringResource(R.string.biometric_use_text),
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
                                    modifier = Modifier.clickableRipple(radius = 10.dp) {
                                        biometricUseState = if (enableBiometric) {
                                            BiometricUseState.DoNotUse
                                        } else {
                                            BiometricUseState.Use
                                        }
                                    },
                                    checked = enableBiometric,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed interface PinLockUseState {
    data object Idle : PinLockUseState
    data object Lock : PinLockUseState
    data object UnLock : PinLockUseState
}