package com.linky.core.data_base.link.data_source

import androidx.paging.PagingSource
import com.linky.core.data_base.link.entity.LinkEntity
import com.linky.core.data_base.link.entity.LinkWithTags
import com.linky.core.data_base.tag.entity.TagEntity
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow

interface LinkDataSource {

    suspend fun insert(linkEntity: LinkEntity, tagEntities: List<TagEntity>): Long

    suspend fun insertLinks(links: List<LinkEntity>): List<Long>

    suspend fun delete(id: Long)

    suspend fun deleteLinks(linkIds: List<Long>)

    suspend fun deleteRemovedAll()

    fun getRemoveLinkCount(): Flow<Int>

    suspend fun findLinkById(id: Long): Link?

    fun selectPage(): PagingSource<Int, LinkWithTags>

    fun selectRemoveAll(): PagingSource<Int, LinkEntity>

    suspend fun incrementReadCount(id: Long)

    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

    suspend fun unRemoveLinks(linkIds: List<Long>)

    fun selectLinksByTagName(tagName: String): PagingSource<Int, LinkWithTags>

}