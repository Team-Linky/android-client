package com.linky.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val getLinksUseCase: GetLinksUseCase,
    private val getTagByIdsUseCase: GetTagByIdsUseCase,
    private val incrementLinkReadCountUseCase: IncrementLinkReadCountUseCase,
    private val linkSetIsRemoveUseCase: LinkSetIsRemoveUseCase
) : ViewModel() {

    val linkList = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { getLinksUseCase.invoke() }
    ).flow.toLinkList().map { pagingData ->
        pagingData.map { link ->
            link.copy(tagList = getTagByIdsUseCase.invoke(link.tags))
        }
    }.cachedIn(viewModelScope).stateIn(
        scope = viewModelScope.plus(Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PagingData.empty()
    )

    fun incrementReadCount(id: Long) {
        viewModelScope.launch {
            incrementLinkReadCountUseCase.invoke(id)
        }
    }

    fun setIsRemoveUseCase(id: Long, isRemove: Boolean) {
        viewModelScope.launch {
            linkSetIsRemoveUseCase.invoke(id, isRemove)
        }
    }

    private fun Flow<PagingData<LinkEntity>>.toLinkList(): Flow<PagingData<Link>> =
        map { list -> list.map { it.toLink() } }

}