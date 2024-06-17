package com.linky.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linky.data.usecase.lock.GetEnableBiometricUseCase
import com.linky.data.usecase.lock.GetEnableLockUseCase
import com.linky.data.usecase.lock.SetEnableBiometricUseCase
import com.linky.data.usecase.lock.SetEnableLockUseCase
import com.linky.lock.state.BiometricStatus
import com.linky.lock.state.LockSideEffect
import com.linky.lock.state.LockState
import com.linky.lock.state.LockStatus
import com.sun5066.common.safe_coroutine.builder.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LinkyLockViewModel @Inject constructor(
    private val getEnableLockUseCase: GetEnableLockUseCase,
    private val getEnableBiometricUseCase: GetEnableBiometricUseCase,
    private val setEnableLockUseCase: SetEnableLockUseCase,
    private val setEnableBiometricUseCase: SetEnableBiometricUseCase,
) : ContainerHost<LockState, LockSideEffect>, ViewModel() {

    override val container = container<LockState, LockSideEffect>(LockState.Init)

    fun doAction(action: Action) {
        when (action) {
            is Action.ToggleLock -> toggleLock(action.enable)
            is Action.ToggleBiometric -> toggleBiometric(action.enable)
        }
    }

    private fun toggleLock(enable: Boolean) {
        viewModelScope.safeLaunch {
            setEnableLockUseCase.invoke(enable)
        }
    }

    private fun toggleBiometric(enable: Boolean) {
        viewModelScope.safeLaunch {
            setEnableBiometricUseCase.invoke(enable)
        }
    }

    init {
        intent {
            getEnableLockUseCase.state.collectLatest {
                reduce { state.copy(lockStatus = LockStatus.Result(it)) }
            }
        }
        intent {
            getEnableBiometricUseCase.state.collectLatest {
                reduce { state.copy(biometricStatus = BiometricStatus.Result(it)) }
            }
        }
    }

}

sealed interface Action {
    data class ToggleLock(val enable: Boolean) : Action
    data class ToggleBiometric(val enable: Boolean) : Action
}