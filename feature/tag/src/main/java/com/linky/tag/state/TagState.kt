package com.linky.tag.state

import androidx.paging.PagingData
import com.linky.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TagState(
    val tags: Flow<PagingData<Tag>>,
) {
    companion object {
        val Init = TagState(
            tags = flowOf(),
        )
    }
}