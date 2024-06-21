package com.linky.pin_setting

import androidx.lifecycle.ViewModel
import com.linky.data.usecase.pin.SavePinUseCase
import com.linky.pin_setting.state.PinSettingSideEffect
import com.linky.pin_setting.state.PinSettingState
import com.linky.pin_setting.state.PinSettingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PinSettingViewModel @Inject constructor(
    private val savePinUseCase: SavePinUseCase
) : ContainerHost<PinSettingState, PinSettingSideEffect>, ViewModel() {

    override val container = container<PinSettingState, PinSettingSideEffect>(PinSettingState.Init)

    fun doAction(action: Action) {
        when (action) {
            is Action.EnterPin -> enterPin(action.pin)
            is Action.ConfirmPin -> confirmPin(action.pin)
        }
    }

    private fun enterPin(pin: String) {
        intent {
            reduce {
                state.copy(
                    status = PinSettingStatus.ConfirmPinScreen,
                    pin = pin
                )
            }
        }
    }

    private fun confirmPin(pin: String) {
        intent {
            if (state.pin == pin) {
                savePinUseCase.invoke(pin)
                postSideEffect(PinSettingSideEffect.PinSaved)
            } else {
                postSideEffect(PinSettingSideEffect.PinMismatch)
            }
        }
    }

}

sealed interface Action {
    data class EnterPin(val pin: String) : Action
    data class ConfirmPin(val pin: String) : Action
}