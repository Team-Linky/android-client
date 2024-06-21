package com.linky.data.link

import androidx.paging.PagingData
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow

interface LinkRepository {

    suspend fun insert(link: Link): Long

    suspend fun delete(id: Long)

    fun selectPage(): Flow<PagingData<Link>>

    suspend fun incrementReadCount(id: Long)

    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

}