package com.linky.timeline.state

import androidx.paging.PagingData
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TimeLineState(
    val links: Flow<PagingData<Link>>,
) {

//    val links = linksState.map { paging ->
//        paging.filter { link ->
//            when (sortType) {
//                Sort.All -> true
//                Sort.Read -> !link.isNoRead
//                Sort.NoRead -> link.isNoRead
//            }
//        }
//    }

    companion object {
        val Init: TimeLineState
            get() = TimeLineState(
                links = flowOf()
            )
    }
}