package com.linky.more_activity.extension

import android.content.Intent
import androidx.activity.ComponentActivity
import com.linky.design_system.animation.slideIn
import com.linky.more_activity.MoreActivity

fun ComponentActivity.launchMoreActivity(startDestination: String) {
    Intent(this, MoreActivity::class.java).apply {
        putExtra("start_destination", startDestination)
        startActivity(this)
        slideIn()
    }
}

val ComponentActivity.startDestination: String?
    get() = intent.getStringExtra("start_destination")