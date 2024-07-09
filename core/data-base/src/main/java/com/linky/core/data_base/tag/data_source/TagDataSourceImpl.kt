package com.linky.core.data_base.tag.data_source

import androidx.paging.PagingSource
import androidx.room.Transaction
import com.linky.core.data_base.link.dao.LinkTagCrossRefDao
import com.linky.core.data_base.link.entity.LinkTagCrossRef
import com.linky.core.data_base.tag.dao.TagDao
import com.linky.core.data_base.tag.entity.TagEntity
import com.linky.core.data_base.tag.entity.TagWithLinkCountEntity
import com.linky.core.data_base.tag.entity.TagWithUsageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagDataSourceImpl @Inject constructor(
    private val tagDao: TagDao,
    private val linkTagCrossRefDao: LinkTagCrossRefDao
) : TagDataSource {

    override suspend fun insert(tagEntity: TagEntity): Long {
        return tagDao.insert(tagEntity)
    }

    override suspend fun insertTags(tags: List<TagEntity>): List<Long> {
        return tagDao.insertTags(tags)
    }

    override suspend fun delete(id: Long) {
        return tagDao.delete(id)
    }

    override suspend fun deleteTagsByIds(tagIds: List<Long>) {
        tagDao.deleteTagsByIds(tagIds)
    }

    override fun getTagCount(): Flow<Int> {
        return tagDao.getTagCount()
    }

    override fun selectAll(): PagingSource<Int, TagEntity> {
        return tagDao.selectAll()
    }

    override fun selectAllWithLinkCount(count: Int): PagingSource<Int, TagEntity> {
        return tagDao.selectAllWithLinkCount(count)
    }

    override suspend fun selectByIds(ids: List<Long>): List<TagEntity> {
        return tagDao.selectByIds(ids)
    }

    @Transaction
    override suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long) {
        tagIds.forEach { tagId ->
            val crossRef = LinkTagCrossRef(linkId = linkId, tagId = tagId)
            linkTagCrossRefDao.insertLinkTagCrossRef(crossRef)
        }
    }

    override fun selectAllWithUsage(linkId: Long): PagingSource<Int, TagWithUsageEntity> {
        return tagDao.selectAllWithUsage(linkId)
    }

    override fun selectTagsWithLinkCount(): PagingSource<Int, TagWithLinkCountEntity> {
        return tagDao.selectTagsWithLinkCount()
    }

}