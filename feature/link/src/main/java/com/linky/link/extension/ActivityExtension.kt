package com.linky.link.extension

import android.content.Intent
import androidx.activity.ComponentActivity
import com.linky.design_system.animation.slideIn
import com.linky.link.LinkActivity

fun ComponentActivity.launchLinkActivity() {
    Intent(this, LinkActivity::class.java).apply {
        startActivity(this)
        slideIn()
    }
}