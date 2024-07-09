package com.linky.core.data_base.tag.data_source

import androidx.paging.PagingSource
import com.linky.core.data_base.tag.entity.TagEntity
import com.linky.core.data_base.tag.entity.TagWithLinkCountEntity
import com.linky.core.data_base.tag.entity.TagWithUsageEntity
import kotlinx.coroutines.flow.Flow

interface TagDataSource {

    suspend fun insert(tagEntity: TagEntity): Long

    suspend fun insertTags(tags: List<TagEntity>): List<Long>

    suspend fun delete(id: Long)

    suspend fun deleteTagsByIds(tagIds: List<Long>)

    fun getTagCount(): Flow<Int>

    fun selectAll(): PagingSource<Int, TagEntity>

    fun selectAllWithLinkCount(count: Int): PagingSource<Int, TagEntity>

    suspend fun selectByIds(ids: List<Long>): List<TagEntity>

    suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long)

    fun selectAllWithUsage(linkId: Long): PagingSource<Int, TagWithUsageEntity>

    fun selectTagsWithLinkCount(): PagingSource<Int, TagWithLinkCountEntity>

}