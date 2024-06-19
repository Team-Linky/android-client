package com.linky.pin.extension

import android.content.Intent
import androidx.activity.ComponentActivity
import com.linky.pin.PinActivity

fun ComponentActivity.launchPinActivity() {
    Intent(this, PinActivity::class.java).apply {
        startActivity(this)
    }
}