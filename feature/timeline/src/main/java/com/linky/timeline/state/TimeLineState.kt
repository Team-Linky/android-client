package com.linky.timeline.state

import androidx.paging.PagingData
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TimeLineState(
    val links: Flow<PagingData<Link>>,
) {
    companion object {
        val Init: TimeLineState
            get() = TimeLineState(
                links = flowOf()
            )
    }
}