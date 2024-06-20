package com.linky.pin

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.fragment.app.FragmentActivity
import com.linky.design_system.animation.slideOut
import com.linky.design_system.ui.theme.LinkyLinkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinActivity : FragmentActivity() {

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        setContent {
            val scaffoldState = rememberScaffoldState()

            LinkyLinkTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    content = { paddingValues ->
                        PinScreen(
                            scaffoldState = scaffoldState,
                            paddingValues = paddingValues,
                            onClose = ::success,
                        )
                    }
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

    private fun success() {
        setResult(RESULT_OK)
        finish()
    }

}