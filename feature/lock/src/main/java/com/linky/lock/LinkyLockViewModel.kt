package com.linky.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linky.data.usecase.lock.IsEnableBiometricUseCase
import com.linky.data.usecase.lock.IsEnableLockUseCase
import com.linky.data.usecase.lock.SetEnableBiometricUseCase
import com.linky.data.usecase.lock.SetEnableLockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LinkyLockViewModel @Inject constructor(
    private val isEnableLockUseCase: IsEnableLockUseCase,
    private val setEnableLockUseCase: SetEnableLockUseCase,
    private val isEnableBiometricUseCase: IsEnableBiometricUseCase,
    private val setEnableBiometricUseCase: SetEnableBiometricUseCase,
) : ContainerHost<LockScreenState, LockScreenSideEffect>, ViewModel() {

    override val container: Container<LockScreenState, LockScreenSideEffect> =
        container(LockScreenState.Loading)

    init {
        getIsEnableLock()
    }

    private fun getIsEnableLock() {
        intent {
            isEnableLockUseCase.invoke()
                .zip(isEnableBiometricUseCase.invoke()) { enableLock, enableBiometric ->
                    reduce { LockScreenState.Success(enableLock, enableBiometric) }
                }.catch { reduce { LockScreenState.Error } }.launchIn(viewModelScope)
        }
    }

    fun enableLock() {
        viewModelScope.launch {
            setEnableLockUseCase.invoke(true)
        }
    }

    fun disableLock() {
        viewModelScope.launch {
            setEnableLockUseCase.invoke(false)
        }
    }

    fun setBiometricUse(isUse: Boolean) {
        viewModelScope.launch {
            setEnableBiometricUseCase.invoke(isUse)
        }
    }

}

sealed interface LockScreenState {
    object Loading : LockScreenState
    data class Success(val enableLock: Boolean, val enableBiometric: Boolean) : LockScreenState
    object Error : LockScreenState
}

sealed interface LockScreenSideEffect {
}