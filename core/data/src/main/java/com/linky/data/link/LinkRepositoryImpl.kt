package com.linky.data.link

import androidx.paging.PagingSource
import com.linky.data_base.link.data_source.LinkDataSource
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.link.entity.LinkEntity.Companion.toEntity
import com.linky.model.Link
import javax.inject.Inject

class LinkRepositoryImpl @Inject constructor(
    private val linkDataSource: LinkDataSource
) : LinkRepository {

    override suspend fun insert(link: Link): Long = linkDataSource.insert(link.toEntity())

    override suspend fun delete(id: Long) = linkDataSource.delete(id)

    override fun selectPage(): PagingSource<Int, LinkEntity> = linkDataSource.selectPage()

    override suspend fun incrementReadCount(id: Long) = linkDataSource.incrementReadCount(id)

    override suspend fun setIsRemoveLink(id: Long, isRemove: Boolean) =
        linkDataSource.setIsRemoveLink(id, isRemove)

}