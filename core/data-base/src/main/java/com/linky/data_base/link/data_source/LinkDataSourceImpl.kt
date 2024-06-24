package com.linky.data_base.link.data_source

import androidx.paging.PagingSource
import androidx.room.Transaction
import com.linky.data_base.link.dao.LinkDao
import com.linky.data_base.link.dao.LinkTagCrossRefDao
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.link.entity.LinkEntity.Companion.toLink
import com.linky.data_base.link.entity.LinkTagCrossRef
import com.linky.data_base.link.entity.LinkWithTags
import com.linky.data_base.tag.dao.TagDao
import com.linky.data_base.tag.entity.TagEntity
import com.linky.model.Link
import javax.inject.Inject

class LinkDataSourceImpl @Inject constructor(
    private val linkDao: LinkDao,
    private val linkTagCrossRefDao: LinkTagCrossRefDao,
) : LinkDataSource {

    @Transaction
    override suspend fun insert(linkEntity: LinkEntity, tagEntities: List<TagEntity>): Long {
        val linkId = linkDao.insert(linkEntity)
        val currentTags = linkDao.findLinkWithTagsById(linkId).tags

        val tagToRemove = currentTags.filter { it !in tagEntities }.mapNotNull { it.id }
        val tagToAdd = tagEntities.filter { it !in currentTags }

        if (tagToRemove.isNotEmpty()) {
            linkTagCrossRefDao.deleteLinkTagCrossRefs(linkId, tagToRemove)
        }

        tagToAdd.forEach { tag ->
            val tagId = tag.id ?: 0L

            linkTagCrossRefDao.insertLinkTagCrossRef(LinkTagCrossRef(linkId, tagId))
        }

        return linkId
    }

    override suspend fun findLinkById(id: Long): Link? {
        return linkDao.findLinkById(id)?.toLink()
    }

    override suspend fun delete(id: Long) {
        linkDao.delete(id)
    }

    override fun selectPage(): PagingSource<Int, LinkWithTags> {
        return linkDao.selectPage()
    }

    override suspend fun incrementReadCount(id: Long) {
        linkDao.incrementReadCount(id)
    }

    @Transaction
    override suspend fun setIsRemoveLink(id: Long, isRemove: Boolean) {
        linkDao.setIsRemoveLink(id, isRemove)
        linkTagCrossRefDao.deleteReferencesByLinkId(id)
    }

    override fun selectLinksByTagName(tagName: String): PagingSource<Int, LinkWithTags> {
        return linkDao.selectLinksByTagName(tagName)
    }

}