package com.linky.data.repository.tag

import com.linky.data_base.tag.data_source.TagDataSource
import com.linky.data_base.tag.entity.TagEntity.Companion.toEntity
import com.linky.data_base.tag.entity.TagEntity.Companion.toTag
import com.linky.model.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDataSource: TagDataSource
) : TagRepository {

    override suspend fun insert(tag: Tag): Long = tagDataSource.insert(tag.toEntity())

    override suspend fun delete(id: Long) = tagDataSource.delete(id)

    override fun selectAll(): Flow<List<Tag>> = tagDataSource.selectAll().toTag()

    override suspend fun selectByIds(ids: List<Long>): List<Tag> = tagDataSource.selectByIds(ids).map { it.toTag() }

    override suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long) = tagDataSource.updateUsedLink(tagIds, linkId)

}