package com.linky.pin_setting.state

sealed interface PinSettingSideEffect {
    data object PinMismatch : PinSettingSideEffect
    data object PinSaved : PinSettingSideEffect
}