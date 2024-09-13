package com.linky.timeline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.data.usecase.link.GetLinksUseCase
import com.linky.data.usecase.link.IncrementLinkReadCountUseCase
import com.linky.data.usecase.link.LinkSetIsRemoveUseCase
import com.linky.data.usecase.link.SelectLinkByTagNameUseCase
import com.linky.timeline.external.INTENT_KEY_TAG_NAME
import com.linky.timeline.state.TimeLineSideEffect
import com.linky.timeline.state.TimeLineState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val getLinksUseCase: GetLinksUseCase,
    private val incrementLinkReadCountUseCase: IncrementLinkReadCountUseCase,
    private val linkSetIsRemoveUseCase: LinkSetIsRemoveUseCase,
    private val selectLinkByTagNameUseCase: SelectLinkByTagNameUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ContainerHost<TimeLineState, TimeLineSideEffect>, ViewModel() {

    override val container = container<TimeLineState, TimeLineSideEffect>(TimeLineState.Init)

    fun doAction(action: TimeLineAction) {
        when (action) {
            is TimeLineAction.IncrementReadCount -> incrementReadCount(action.id)
            is TimeLineAction.RemoveTimeLine -> removeTimeLine(action.id)
        }
    }

    private fun incrementReadCount(id: Long?) {
        viewModelScope.safeLaunch {
            incrementLinkReadCountUseCase.invoke(id!!)
        }
    }

    private fun removeTimeLine(id: Long?) {
        viewModelScope.safeLaunch {
            linkSetIsRemoveUseCase.invoke(id!!, true)
        }
    }

    private fun getLinks() {
        intent {
            val links = savedStateHandle.get<String>(INTENT_KEY_TAG_NAME)
                ?.let { selectLinkByTagNameUseCase.invoke(it) }
                ?: getLinksUseCase.invoke()

            reduce { state.copy(links = links.cachedIn(viewModelScope)) }
        }
    }

    init {
        getLinks()
    }
}

sealed interface TimeLineAction {
    data class IncrementReadCount(val id: Long?) : TimeLineAction
    data class RemoveTimeLine(val id: Long?) : TimeLineAction
}