package com.linky.feature.tag_modifier.state

import androidx.paging.PagingData
import com.linky.model.TagWithUsage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TagModifierState(
    val mode: Mode,
    val tags: Flow<PagingData<TagWithUsage>>,
) {
    companion object {
        val Init = TagModifierState(
            mode = Mode.None,
            tags = flowOf()
        )
    }
}

enum class Mode {
    None, Creator, Editor
}