package com.linky.link.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.linky.design_system.animation.slideIn
import com.linky.link.LinkActivity

@Composable
fun rememberLaunchLinkActivityResult(
    onSuccess: (Bundle?) -> Unit,
    onCancel: () -> Unit
) = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    when (result.resultCode) {
        Activity.RESULT_OK -> onSuccess.invoke(result.data?.extras)
        Activity.RESULT_CANCELED -> onCancel.invoke()
    }
}

fun ManagedActivityResultLauncher<Intent, ActivityResult>.launchLinkActivity(
    activity: ComponentActivity,
    startDestination: String? = null,
    mode: Int = 0,
    url: String? = null,
    linkId: Long? = null,
) {
    Intent(activity, LinkActivity::class.java).apply {
        putExtra("startDestination", startDestination)
        putExtra("mode", mode)
        putExtra("url", Uri.encode(url))
        putExtra("linkId", linkId)
        launch(this)
        activity.slideIn()
    }
}

fun ComponentActivity.launchLinkActivity(
    startDestination: String? = null,
    mode: Int = 0,
    url: String? = null,
    linkId: Long? = null,
) {
    Intent(this, LinkActivity::class.java).apply {
        putExtra("startDestination", startDestination)
        putExtra("mode", mode)
        putExtra("url", Uri.encode(url))
        putExtra("linkId", linkId)
        startActivity(this)
        slideIn()
    }
}