package com.linky.pin_setting.extension

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.linky.design_system.animation.slideIn
import com.linky.pin_setting.PinSettingActivity

@Composable
fun rememberLauncherForPinSettingActivityResult(
    action: (Boolean) -> Unit
) = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    action.invoke(result.resultCode == Activity.RESULT_OK)
}

fun ManagedActivityResultLauncher<Intent, ActivityResult>.launchCRA(
    activity: ComponentActivity
) {
    Intent(activity, PinSettingActivity::class.java).also { intent ->
        launch(intent)
        activity.slideIn()
    }
}