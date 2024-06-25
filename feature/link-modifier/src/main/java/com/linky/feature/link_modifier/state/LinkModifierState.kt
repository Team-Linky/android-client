package com.linky.feature.link_modifier.state

import androidx.paging.PagingData
import com.linky.model.Link
import com.linky.model.TagWithUsage
import com.linky.model.open_graph.OpenGraphData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class LinkModifierState(
    val mode: Mode,
    val link: Link?,
    val tags: Flow<PagingData<TagWithUsage>>,
    val openGraphStatus: OpenGraphStatus,
    val linkSaveStatus: LinkSaveStatus,
) {
    companion object {
        val Init get() = LinkModifierState(
            mode = Mode.Creator,
            link = null,
            tags = flowOf(),
            openGraphStatus = OpenGraphStatus.Idle,
            linkSaveStatus = LinkSaveStatus.Idle,
        )
    }
}

enum class Mode {
    Creator, Editor
}

sealed interface OpenGraphStatus {
    data object Idle : OpenGraphStatus
    data class Error(val message: String) : OpenGraphStatus
    data class Success(val openGraphData: OpenGraphData) : OpenGraphStatus
}

sealed interface LinkSaveStatus {
    data object Idle : LinkSaveStatus
    data object Loading : LinkSaveStatus
    data object Error : LinkSaveStatus
    data object Success : LinkSaveStatus
}