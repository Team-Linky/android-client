package com.linky.certification_registration.extension

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.linky.certification_registration.CertificationRegistrationActivity
import com.linky.design_system.animation.slideIn

@Composable
fun ComponentActivity.LaunchCertificationRegistrationActivity(
    onSuccess: () -> Unit,
    onFail: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onSuccess.invoke()
        } else {
            onFail.invoke()
        }
    }
    SideEffect {
        Intent(this, CertificationRegistrationActivity::class.java).apply {
            launcher.launch(this)
            slideIn()
        }
    }
}