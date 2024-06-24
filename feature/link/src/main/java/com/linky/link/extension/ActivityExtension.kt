package com.linky.link.extension

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import com.linky.design_system.animation.slideIn
import com.linky.link.LinkActivity

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