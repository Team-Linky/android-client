package com.linky.data_base.link.data_source

import androidx.paging.PagingSource
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.link.entity.LinkWithTags
import com.linky.data_base.tag.entity.TagEntity
import com.linky.model.Link

interface LinkDataSource {

    suspend fun insert(linkEntity: LinkEntity, tagEntities: List<TagEntity>): Long

    suspend fun findLinkById(id: Long): Link?

    suspend fun delete(id: Long)

    fun selectPage(): PagingSource<Int, LinkWithTags>

    suspend fun incrementReadCount(id: Long)

    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

    fun selectLinksByTagName(tagName: String): PagingSource<Int, LinkWithTags>

}