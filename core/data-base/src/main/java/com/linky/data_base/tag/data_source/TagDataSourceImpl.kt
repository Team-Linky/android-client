package com.linky.data_base.tag.data_source

import androidx.paging.PagingSource
import com.linky.data_base.link.dao.LinkTagCrossRefDao
import com.linky.data_base.link.entity.LinkTagCrossRef
import com.linky.data_base.tag.dao.TagDao
import com.linky.data_base.tag.entity.TagEntity
import javax.inject.Inject

class TagDataSourceImpl @Inject constructor(
    private val tagDao: TagDao,
    private val linkTagCrossRefDao: LinkTagCrossRefDao
) : TagDataSource {

    override suspend fun insert(tagEntity: TagEntity): Long = tagDao.insert(tagEntity)

    override suspend fun delete(id: Long) = tagDao.delete(id)

    override fun selectAll(): PagingSource<Int, TagEntity> = tagDao.selectAll()

    override suspend fun selectByIds(ids: List<Long>): List<TagEntity> = tagDao.selectByIds(ids)

    override suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long) {
        tagIds.forEach { tagId ->
            val crossRef = LinkTagCrossRef(linkId = linkId, tagId = tagId)
            linkTagCrossRefDao.insertLinkTagCrossRef(crossRef)
        }
    }

}