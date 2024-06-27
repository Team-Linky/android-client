package com.linky.pin.extension

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.linky.pin.PinActivity

@Composable
fun rememberLaunchPinActivityResult(
    onSuccess: () -> Unit,
    onCancel: () -> Unit
) = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    when (result.resultCode) {
        Activity.RESULT_OK -> onSuccess.invoke()
        Activity.RESULT_CANCELED -> onCancel.invoke()
    }
}

fun ManagedActivityResultLauncher<Intent, ActivityResult>.launchPinActivity(
    activity: ComponentActivity
) {
    Intent(activity, PinActivity::class.java).apply { launch(this) }
}

fun ComponentActivity.launchPinActivity() {
    Intent(this, PinActivity::class.java).apply {
        startActivity(this)
    }
}

fun ActivityResultLauncher<Intent>.launchPinActivity(activity: ComponentActivity) {
    Intent(activity, PinActivity::class.java).apply {
        launch(this)
    }
}