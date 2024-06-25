package com.linky.feature.tag_setting.state

import androidx.paging.PagingData
import com.linky.model.TagWithLinkCount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TagSettingState(
    val tagCount: Int,
    val tagWithLinkCounts: Flow<PagingData<TagWithLinkCount>>
) {
    companion object {
        val Init get() = TagSettingState(
            tagCount = 0,
            tagWithLinkCounts = flowOf()
        )
    }
}