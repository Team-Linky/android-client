package com.linky.lock

import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.linky.certification_registration.extension.LaunchCertificationRegistrationActivity
import com.linky.design_system.ui.theme.LockContentLineColor
import com.linky.lock.component.BiometricsRecognitionContent
import com.linky.lock.component.LockContent
import com.linky.lock.component.LockHeader
import com.linky.lock.component.PasswordChangeContent
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
    when (val state = viewModel.collectAsState().value) {
        is LockScreenState.Loading -> Unit
        is LockScreenState.Error -> Unit
        is LockScreenState.Success -> {
            LinkyLockScreen(
                initEnableLock = state.enableLock,
                initEnableBiometric = state.enableBiometric,
                onBack = onBack,
                enableLockValue = viewModel::enableLock,
                disableLockValue = viewModel::disableLock,
                onChangeBiometricValue = { viewModel.setBiometricUse(it) }
            )
        }
    }
}

@Composable
private fun LinkyLockScreen(
    initEnableLock: Boolean,
    initEnableBiometric: Boolean,
    onBack: () -> Unit = {},
    enableLockValue: () -> Unit = {},
    disableLockValue: () -> Unit = {},
    onChangeBiometricValue: (Boolean) -> Unit = {},
) {
    val activity = LocalContext.current as ComponentActivity
    var biometricStatus by remember { mutableStateOf<BiometricStatus>(BiometricStatus.Initialize) }
    var enableLock by remember { mutableStateOf(initEnableLock) }
    var enableBiometric by remember { mutableStateOf(initEnableBiometric) }

    if (enableLock && !initEnableLock) {
        activity.LaunchCertificationRegistrationActivity(
            onSuccess = {
                enableLock = true
                enableLockValue.invoke()
            },
            onFail = {
                enableLock = false
                disableLockValue.invoke()
            }
        )
    }

    if (initEnableLock && !enableLock) {
        disableLockValue.invoke()
    }

    if (enableBiometric && biometricStatus is BiometricStatus.Success) {
        onChangeBiometricValue.invoke(true)
    } else {
        enableBiometric = false
        onChangeBiometricValue.invoke(false)
    }

    LaunchedEffect(Unit) {
        val authenticators = BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        biometricStatus = when (BiometricManager.from(activity.applicationContext).canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.Success
            else -> BiometricStatus.Fail
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LockHeader(onBack)
        LockContent(
            checked = enableLock,
            onCheckedChange = { enableLock = it }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LockContentLineColor)
        )
        if (initEnableLock && enableLock) {
            PasswordChangeContent {}
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(LockContentLineColor)
            )
            BiometricsRecognitionContent(
                value = enableBiometric,
                biometricStatus = biometricStatus,
                onCheckedChange = { enableBiometric = it }
            )
        }
    }
}

sealed interface BiometricStatus {
    object Initialize : BiometricStatus
    object Success : BiometricStatus
    object Fail : BiometricStatus
}