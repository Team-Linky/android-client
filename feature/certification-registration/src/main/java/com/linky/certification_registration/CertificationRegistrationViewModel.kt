package com.linky.certification_registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linky.data.usecase.certification.SetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CertificationRegistrationViewModel @Inject constructor(
    private val setPasswordUseCase: SetPasswordUseCase
) : ContainerHost<State, SideEffect>, ViewModel() {

    override val container: Container<State, SideEffect> = container(State.FirstInputScreen)

    private var tempPassword = ""
    val tempPasswordIsNotEmpty: Boolean get() = tempPassword.isNotEmpty()

    fun setFirstPassword(password: String) {
        tempPassword = password

        intent {
            reduce { State.SecondInputScreen }
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            intent {
                if (tempPassword == password) {
                    setPasswordUseCase.invoke(password)
                    postSideEffect(SideEffect.RegisterSuccess)
                } else {
                    postSideEffect(SideEffect.RegisterFail)
                }
            }
        }
    }

}

sealed interface State {
    object FirstInputScreen : State
    object SecondInputScreen : State
}

sealed interface SideEffect {
    object RegisterSuccess : SideEffect
    object RegisterFail : SideEffect
}