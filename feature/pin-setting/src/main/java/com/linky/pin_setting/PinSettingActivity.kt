package com.linky.pin_setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.theme.LinkyLinkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinSettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkyLinkTheme {
                PinSettingScreen(
                    onBack = ::back,
                    onComplete = ::complete
                )
            }
        }
    }

    override fun onPause() {
        if (isFinishing) {
            slideOut()
        }
        super.onPause()
    }

    private fun back() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun complete() {
        setResult(RESULT_OK)
        finish()
    }
}