package com.linky.pin_setting.extension

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.linky.pin_setting.PinSettingActivity
import com.linky.design_system.animation.slideIn

@Composable
fun rememberLauncherForPinSettingActivityResult(
    onSuccess: () -> Unit,
    onFail: () -> Unit,
    onComplete: () -> Unit,
) = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        onSuccess.invoke()
    } else {
        onFail.invoke()
    }
    onComplete.invoke()
}

fun ManagedActivityResultLauncher<Intent, ActivityResult>.launchCRA(
    activity: ComponentActivity,
    enableLock: Boolean
) {
    Intent(activity, PinSettingActivity::class.java).also { intent ->
        intent.putExtra("enableLock", enableLock)
        launch(intent)
        activity.slideIn()
    }
}