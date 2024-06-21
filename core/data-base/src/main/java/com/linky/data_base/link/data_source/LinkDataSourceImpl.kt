package com.linky.data_base.link.data_source

import androidx.paging.PagingSource
import com.linky.data_base.link.dao.LinkDao
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.link.entity.LinkWithTags
import com.linky.data_base.tag.entity.TagEntity
import javax.inject.Inject

class LinkDataSourceImpl @Inject constructor(
    private val linkDao: LinkDao
) : LinkDataSource {

    override suspend fun insert(
        linkEntity: LinkEntity,
        tagEntities: List<TagEntity>
    ): Long = linkDao.insertLinkWithTags(linkEntity, tagEntities)

    override suspend fun delete(id: Long) = linkDao.delete(id)

    override fun selectPage(): PagingSource<Int, LinkWithTags> = linkDao.selectPage()

    override suspend fun incrementReadCount(id: Long) = linkDao.incrementReadCount(id)

    override suspend fun setIsRemoveLink(id: Long, isRemove: Boolean) =
        linkDao.setIsRemoveLink(id, isRemove)

}