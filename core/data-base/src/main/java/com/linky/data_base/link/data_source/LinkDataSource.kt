package com.linky.data_base.link.data_source

import androidx.paging.PagingSource
import com.linky.data_base.link.entity.LinkEntity

interface LinkDataSource {

    suspend fun insert(linkEntity: LinkEntity): Long

    suspend fun delete(id: Long)

    fun selectPage(): PagingSource<Int, LinkEntity>

    suspend fun incrementReadCount(id: Long)

    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

}