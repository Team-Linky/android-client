package com.linky.common.biometric_compose

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

sealed interface BiometricUseState {
    data object Idle : BiometricUseState
    data object Use : BiometricUseState
    data object DoNotUse : BiometricUseState
}

class BiometricClient {

    fun launch(
        activity: FragmentActivity,
        authBiometric: BiometricPrompt.AuthenticationCallback,
        title: String,
        subtitle: String? = null,
        description: String? = null,
        negativeButtonText: String
    ) {
        val executor = ContextCompat.getMainExecutor(activity.applicationContext)
        val biometricPrompt = BiometricPrompt(activity, executor, authBiometric)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeButtonText)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

}

@Composable
fun LaunchedBiometric(
    key: Any = Unit,
    biometricUseState: BiometricUseState,
    onUse: BiometricClient.() -> Unit = {},
    onDoNotUse: BiometricClient.() -> Unit = {},
) {
    val useState by rememberUpdatedState(onUse)
    val doNotUseState by rememberUpdatedState(onDoNotUse)

    val launchBiometric = remember(key) {
        BiometricClient()
    }

    LaunchedEffect(biometricUseState) {
        when (biometricUseState) {
            is BiometricUseState.Idle -> Unit
            is BiometricUseState.Use -> useState.invoke(launchBiometric)
            is BiometricUseState.DoNotUse -> doNotUseState.invoke(launchBiometric)
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

@Composable
fun rememberAuthentication(
    key: Any = Unit,
    onError: () -> Unit = {},
    onFail: () -> Unit = {},
    onSuccess: () -> Unit = {},
) = remember(key) {
    object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onError.invoke()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onFail.invoke()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onSuccess.invoke()
        }
    }
}