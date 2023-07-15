package com.linky.certification.extension

import android.content.Intent
import androidx.activity.ComponentActivity
import com.linky.certification.CertificationActivity
import com.linky.design_system.animation.slideIn

fun ComponentActivity.launchCertificationActivity() {
    Intent(this, CertificationActivity::class.java).apply {
        startActivity(this)
        slideIn()
    }
}