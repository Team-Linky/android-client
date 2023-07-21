package com.linky.data_base.tag.data_source

import com.linky.data_base.tag.dao.TagDao
import com.linky.data_base.tag.entity.TagEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagDataSourceImpl @Inject constructor(
    private val tagDao: TagDao
) : TagDataSource {

    override suspend fun insert(tagEntity: TagEntity): Long = tagDao.insert(tagEntity)

    override suspend fun delete(id: Long) = tagDao.delete(id)

    override fun selectAll(): Flow<List<TagEntity>> = tagDao.selectAll()

    override suspend fun selectByIds(ids: List<Long>): List<TagEntity> = tagDao.selectByIds(ids)

    override suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long) {
        tagIds.forEach { tagId ->
            val entity = tagDao.findById(tagId)
            val linkIds = entity.linkIds?.toMutableList()?.apply {
                add(linkId)
            } ?: listOf(linkId)
            tagDao.insert(entity.copy(linkIds = linkIds))
        }
    }

}