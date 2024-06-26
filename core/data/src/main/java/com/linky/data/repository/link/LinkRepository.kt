package com.linky.data.repository.link

import androidx.paging.PagingData
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow

interface LinkRepository {

    suspend fun insert(link: Link): Long

    suspend fun insertLinks(links: List<Link>): List<Long>

    suspend fun delete(id: Long)

    suspend fun deleteLinks(linkIds: List<Long>)

    suspend fun deleteRemovedAll()

    fun getRemoveLinkCount(): Flow<Int>

    suspend fun findLinkById(id: Long): Link?

    fun selectPage(): Flow<PagingData<Link>>

    fun selectRemoveAll(): Flow<PagingData<Link>>

    suspend fun incrementReadCount(id: Long)

    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

    fun selectLinksByTagName(tagName: String): Flow<PagingData<Link>>

    suspend fun unRemoveLinks(linkIds: List<Long>)

}