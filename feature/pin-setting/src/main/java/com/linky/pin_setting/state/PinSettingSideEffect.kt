package com.linky.pin_setting.state

sealed interface PinSettingSideEffect {
    data object PINMismatch : PinSettingSideEffect
    data object PINSaved : PinSettingSideEffect
}