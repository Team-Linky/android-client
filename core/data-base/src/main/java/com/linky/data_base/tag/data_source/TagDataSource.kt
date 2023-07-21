package com.linky.data_base.tag.data_source

import com.linky.data_base.tag.entity.TagEntity
import kotlinx.coroutines.flow.Flow

interface TagDataSource {

    suspend fun insert(tagEntity: TagEntity): Long

    suspend fun delete(id: Long)

    fun selectAll(): Flow<List<TagEntity>>

    suspend fun selectByIds(ids: List<Long>): List<TagEntity>

    suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long)

}