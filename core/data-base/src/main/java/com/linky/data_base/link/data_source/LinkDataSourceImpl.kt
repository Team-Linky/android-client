package com.linky.data_base.link.data_source

import androidx.paging.PagingSource
import com.linky.data_base.link.dao.LinkDao
import com.linky.data_base.link.entity.LinkEntity
import javax.inject.Inject

class LinkDataSourceImpl @Inject constructor(
    private val linkDao: LinkDao
) : LinkDataSource {

    override suspend fun insert(linkEntity: LinkEntity): Long = linkDao.insert(linkEntity)

    override suspend fun delete(id: Long) = linkDao.delete(id)

    override fun selectPage(): PagingSource<Int, LinkEntity> = linkDao.selectPage()

    override suspend fun incrementReadCount(id: Long) = linkDao.incrementReadCount(id)

}