package com.linky.webview.extension

import android.content.Intent
import androidx.activity.ComponentActivity
import com.linky.design_system.animation.slideIn
import com.linky.webview.WebViewActivity

fun ComponentActivity.launchWebViewActivity(pageUrl: String) {
    Intent(this, WebViewActivity::class.java).apply {
        putExtra("pageUrl", pageUrl)
        startActivity(this)
        slideIn()
    }
}

val ComponentActivity.pageUrl: String?
    get() = intent.getStringExtra("pageUrl")