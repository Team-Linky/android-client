package com.linky.timeline.state

import androidx.paging.PagingData
import androidx.paging.filter
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

data class TimeLineState(
    val sortType: Sort,
    val sortList: List<Sort>,
    private val linksState: Flow<PagingData<Link>>,
) {

    val links = linksState.map { paging ->
        paging.filter { link ->
            when (sortType) {
                is Sort.All -> true
                is Sort.Read -> !link.isNoRead
                is Sort.NoRead -> link.isNoRead
            }
        }
    }

    companion object {
        val Init: TimeLineState
            get() = TimeLineState(
                sortType = Sort.All,
                sortList = listOf(Sort.All, Sort.Read, Sort.NoRead),
                linksState = flowOf()
            )
    }
}