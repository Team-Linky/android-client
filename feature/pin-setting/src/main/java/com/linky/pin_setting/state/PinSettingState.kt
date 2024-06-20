package com.linky.pin_setting.state

import androidx.annotation.StringRes
import com.linky.design_system.R

data class PinSettingState(
    val status: PinSettingStatus,
    val pin: String,
) {
    companion object {
        val Init: PinSettingState
            get() = PinSettingState(
                status = PinSettingStatus.EnterPinScreen,
                pin = "",
            )
    }

    val titleRes: Int
        @StringRes get() = if (pin.isNotEmpty()) {
            R.string.pin_setting_sub_title
        } else {
            R.string.pin_setting_title
        }
}

sealed interface PinSettingStatus {
    data object EnterPinScreen : PinSettingStatus
    data object ConfirmPinScreen : PinSettingStatus
}