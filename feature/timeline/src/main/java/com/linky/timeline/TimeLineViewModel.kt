package com.linky.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.linky.data.usecase.link.GetLinksUseCase
import com.linky.data.usecase.link.IncrementLinkReadCountUseCase
import com.linky.data.usecase.link.LinkSetIsRemoveUseCase
import com.linky.data.usecase.tag.GetTagByIdsUseCase
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.link.entity.LinkEntity.Companion.toLink
import com.linky.model.Link
import com.linky.timeline.state.TimeLineSideEffect
import com.linky.timeline.state.TimeLineState
import com.sun5066.common.safe_coroutine.builder.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val getLinksUseCase: GetLinksUseCase,
    private val getTagByIdsUseCase: GetTagByIdsUseCase,
    private val incrementLinkReadCountUseCase: IncrementLinkReadCountUseCase,
    private val linkSetIsRemoveUseCase: LinkSetIsRemoveUseCase
) : ViewModel(), ContainerHost<TimeLineState, TimeLineSideEffect> {

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

    private fun Flow<PagingData<LinkEntity>>.toLinkList(): Flow<PagingData<Link>> =
        map { pagingData ->
            pagingData.map { linkEntity ->
                linkEntity.toLink().copy(tagList = getTagByIdsUseCase.invoke(linkEntity.tags))
            }
        }

    init {
        intent {
            val links = getLinksUseCase.invoke()
                .toLinkList()
                .cachedIn(viewModelScope)

            reduce { state.copy(links = links) }
        }
    }
}

sealed interface TimeLineAction {
    data class IncrementReadCount(val id: Long?) : TimeLineAction
    data class RemoveTimeLine(val id: Long?) : TimeLineAction
}