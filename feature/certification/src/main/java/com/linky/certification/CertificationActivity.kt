package com.linky.certification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.theme.LinkyLinkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CertificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkyLinkTheme {
                CertificationScreen(::finish)
            }
        }
    }

    override fun onPause() {
        if (isFinishing) {
            slideOut()
        }
        super.onPause()
    }

    override fun onBackPressed() {

    }
}