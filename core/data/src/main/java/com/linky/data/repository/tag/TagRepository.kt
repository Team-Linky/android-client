package com.linky.data.repository.tag

import androidx.paging.PagingData
import com.linky.model.Tag
import com.linky.model.TagWithLinkCount
import com.linky.model.TagWithUsage
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    val deletedTagsCache: List<Tag>

    suspend fun insert(tag: Tag): Long

    suspend fun insertTags(tags: List<Tag>): List<Long>

    suspend fun delete(id: Long)

    suspend fun deleteTagsByIds(tags: List<Tag>)

    fun getTagCount(): Flow<Int>

    fun selectAll(): Flow<PagingData<Tag>>

    fun selectAllWithLinkCount(count: Int): Flow<PagingData<Tag>>

    suspend fun selectByIds(ids: List<Long>): List<Tag>

    suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long)

    fun selectAllWithUsage(linkId: Long, pageSize: Int): Flow<PagingData<TagWithUsage>>

    fun selectTagsWithLinkCount(): Flow<PagingData<TagWithLinkCount>>

}