package com.linky.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linky.data.usecase.certification.CertifiedUseCase
import com.sun5066.common.safe_coroutine.builder.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val certifiedUseCase: CertifiedUseCase
) : ViewModel() {

    private val _sideEffect = Channel<PinSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
    )

    fun doAction(action: Action) {
        when (action) {
            is Action.PinCheck -> pinCheck(action.pin)
        }
    }

    private fun pinCheck(pin: String) {
        viewModelScope.safeLaunch {
            val success = certifiedUseCase.invoke(pin)

            if (success) {
                _sideEffect.send(PinSideEffect.Finish)
            } else {
                _sideEffect.send(PinSideEffect.PinMismatch)
            }
        }
    }

}

sealed interface Action {
    data class PinCheck(val pin: String) : Action
}

sealed interface PinSideEffect {
    data object PinMismatch : PinSideEffect
    data object Finish : PinSideEffect
}