package com.linky.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.linky.common.safe_coroutine.builder.safeLaunch
import com.linky.data.usecase.link.GetLinksUseCase
import com.linky.data.usecase.link.IncrementLinkReadCountUseCase
import com.linky.data.usecase.link.LinkSetIsRemoveUseCase
import com.linky.timeline.state.Sort
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
    private val linkSetIsRemoveUseCase: LinkSetIsRemoveUseCase
) : ContainerHost<TimeLineState, TimeLineSideEffect>, ViewModel() {

    override val container = container<TimeLineState, TimeLineSideEffect>(TimeLineState.Init)

    fun doAction(action: TimeLineAction) {
        when (action) {
            is TimeLineAction.IncrementReadCount -> incrementReadCount(action.id)
            is TimeLineAction.RemoveTimeLine -> removeTimeLine(action.id)
            is TimeLineAction.ChangeSort -> changeSort(action.sort)
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

    private fun changeSort(sort: Sort) {
        intent {
            reduce { state.copy(sortType = sort) }
        }
    }

    init {
        intent {
            val links = getLinksUseCase.invoke().cachedIn(viewModelScope)

            reduce { state.copy(linksState = links) }
        }
    }
}

sealed interface TimeLineAction {
    data class IncrementReadCount(val id: Long?) : TimeLineAction
    data class RemoveTimeLine(val id: Long?) : TimeLineAction
    data class ChangeSort(val sort: Sort) : TimeLineAction
}