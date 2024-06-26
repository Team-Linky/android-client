package com.linky.pin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linky.data.usecase.pin.GetEnablePinUseCase
import com.linky.data.usecase.lock.GetEnableBiometricUseCase
import com.linky.common.safe_coroutine.builder.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val getEnablePinUseCase: GetEnablePinUseCase,
    private val getEnableBiometricUseCase: GetEnableBiometricUseCase,
) : ViewModel() {

    private val _sideEffect = Channel<PinSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
    )

    var enableBiometric by mutableStateOf(false)
        private set

    fun doAction(action: Action) {
        when (action) {
            is Action.PinCheck -> pinCheck(action.pin)
        }
    }

    private fun pinCheck(pin: String) {
        viewModelScope.safeLaunch {
            val success = getEnablePinUseCase.invoke(pin)

            if (success) {
                _sideEffect.send(PinSideEffect.Finish)
            } else {
                _sideEffect.send(PinSideEffect.PinMismatch)
            }
        }
    }

    private fun updateEnableBiometric() {
        viewModelScope.safeLaunch {
            enableBiometric = getEnableBiometricUseCase.state.first()
        }
    }

    init {
        updateEnableBiometric()
    }

}

sealed interface Action {
    data class PinCheck(val pin: String) : Action
}

sealed interface PinSideEffect {
    data object PinMismatch : PinSideEffect
    data object Finish : PinSideEffect
}