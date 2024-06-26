package com.linky.feature.recycle_bin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.linky.data.usecase.link.DeleteLinksUseCase
import com.linky.data.usecase.link.DeleteRemovedAllUseCase
import com.linky.data.usecase.link.GetRemoveLinkCountUseCase
import com.linky.data.usecase.link.LinkSelectRemoveAllUseCase
import com.linky.data.usecase.link.UnRemoveLinksUseCase
import com.linky.feature.recycle_bin.RecycleBinState.Companion.Init
import com.linky.model.Link
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LinkRecycleBinViewModel @Inject constructor(
    private val getRemoveLinkCountUseCase: GetRemoveLinkCountUseCase,
    private val linkSelectRemoveAllUseCase: LinkSelectRemoveAllUseCase,
    private val unRemoveLinksUseCase: UnRemoveLinksUseCase,
    private val deleteLinksUseCase: DeleteLinksUseCase,
    private val deleteRemovedAllUseCase: DeleteRemovedAllUseCase,
) : ContainerHost<RecycleBinState, RecycleBinSideEffect>, ViewModel() {

    override val container = container<RecycleBinState, RecycleBinSideEffect>(Init)

    fun doAction(action: RecycleBinAction) {
        when (action) {
            is RecycleBinAction.RecycleLinks -> recycleLinks(action.links)
            is RecycleBinAction.DeleteLinks -> deleteLinks(action.links)
            is RecycleBinAction.ClearAll -> clearAll()
        }
    }

    private fun getLinksCount() {
        intent {
            getRemoveLinkCountUseCase.invoke().collectLatest { count ->
                reduce { state.copy(linksCount = count) }
            }
        }
    }

    private fun getLinks() {
        intent {
            val links = linkSelectRemoveAllUseCase.invoke().cachedIn(viewModelScope)

            reduce { state.copy(links = links) }
        }
    }

    private fun recycleLinks(links: List<Link>) {
        intent {
            val linkIds = links.mapNotNull { it.id }

            unRemoveLinksUseCase.invoke(linkIds)
        }
    }

    private fun deleteLinks(links: List<Link>) {
        intent {
            val linkIds = links.mapNotNull { it.id }

            deleteLinksUseCase.invoke(linkIds)
        }
    }

    private fun clearAll() {
        intent {
            deleteRemovedAllUseCase.invoke()
        }
    }

    init {
        getLinksCount()
        getLinks()
    }

}

sealed interface RecycleBinAction {
    data class RecycleLinks(val links: List<Link>) : RecycleBinAction
    data class DeleteLinks(val links: List<Link>) : RecycleBinAction
    data object ClearAll : RecycleBinAction
}

data class RecycleBinState(
    val linksCount: Int,
    val links: Flow<PagingData<Link>>
) {
    companion object {
        val Init get() = RecycleBinState(
            linksCount = 0,
            links = flowOf()
        )
    }
}

sealed interface RecycleBinSideEffect {

}