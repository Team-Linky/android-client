package com.linky.data.repository.tag

import androidx.paging.PagingData
import com.linky.model.Tag
import com.linky.model.TagWithUsage
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    suspend fun insert(tag: Tag): Long

    suspend fun delete(id: Long)

    fun selectAll(): Flow<PagingData<Tag>>

    fun selectAllWithLinkCount(count: Int): Flow<PagingData<Tag>>

    suspend fun selectByIds(ids: List<Long>): List<Tag>

    suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long)

    fun selectAllWithUsage(linkId: Long, pageSize: Int): Flow<PagingData<TagWithUsage>>

}