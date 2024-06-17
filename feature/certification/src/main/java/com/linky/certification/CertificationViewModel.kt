package com.linky.certification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linky.data.usecase.certification.CertifiedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val certifiedUseCase: CertifiedUseCase
) : ContainerHost<State, SideEffect>, ViewModel() {

    override val container: Container<State, SideEffect> = container(State.Initialize)

    fun certified(password: String) {
        intent {
            certifiedUseCase.invoke(password).onEach { isSuccess ->
                if (isSuccess) {
                    postSideEffect(SideEffect.CertifiedSuccess)
                } else {
                    postSideEffect(SideEffect.CertifiedFail)
                }
            }.launchIn(viewModelScope)
        }
    }

}

sealed interface State {
    data object Initialize : State
}

sealed interface SideEffect {
    data object CertifiedSuccess : SideEffect
    data object CertifiedFail : SideEffect
}