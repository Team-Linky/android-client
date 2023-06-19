package com.linky.intercation.vibrate

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun Vibrator.vibrateCompat(milliseconds: Long = 5, amplitude: Int = 255) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createOneShot(milliseconds, amplitude))
    } else {
        vibrate(milliseconds)
    }
}