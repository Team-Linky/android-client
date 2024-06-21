package com.linky.data_base.tag.data_source

import androidx.paging.PagingSource
import com.linky.data_base.tag.entity.TagEntity

interface TagDataSource {

    suspend fun insert(tagEntity: TagEntity): Long

    suspend fun delete(id: Long)

    fun selectAll(): PagingSource<Int, TagEntity>

    suspend fun selectByIds(ids: List<Long>): List<TagEntity>

    suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long)

}