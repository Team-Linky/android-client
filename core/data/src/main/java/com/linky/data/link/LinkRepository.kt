package com.linky.data.link

import androidx.paging.PagingSource
import com.linky.data_base.link.entity.LinkEntity
import com.linky.model.Link

interface LinkRepository {

    suspend fun insert(link: Link): Long

    suspend fun delete(id: Long)

    fun selectPage(): PagingSource<Int, LinkEntity>

    suspend fun incrementReadCount(id: Long)

}