package com.linky.data.repository.link

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.linky.data_base.link.data_source.LinkDataSource
import com.linky.data_base.link.entity.LinkEntity.Mapper.toEntity
import com.linky.data_base.link.entity.LinkEntity.Mapper.toLinks
import com.linky.data_base.link.entity.LinkWithTags.Mapper.toLinks
import com.linky.data_base.tag.entity.TagEntity.Companion.toEntity
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LinkRepositoryImpl @Inject constructor(
    private val linkDataSource: LinkDataSource
) : LinkRepository {

    override suspend fun insert(link: Link): Long {
        return linkDataSource.insert(link.toEntity(), link.tags.map { it.toEntity() })
    }

    override suspend fun insertLinks(links: List<Link>): List<Long> {
        return linkDataSource.insertLinks(links.map { it.toEntity() })
    }

    override suspend fun delete(id: Long) {
        linkDataSource.delete(id)
    }

    override suspend fun deleteLinks(linkIds: List<Long>) {
        linkDataSource.deleteLinks(linkIds)
    }

    override suspend fun deleteRemovedAll() {
        linkDataSource.deleteRemovedAll()
    }

    override fun getRemoveLinkCount(): Flow<Int> {
        return linkDataSource.getRemoveLinkCount()
    }

    override suspend fun findLinkById(id: Long): Link? {
        return linkDataSource.findLinkById(id)
    }

    override fun selectPage(): Flow<PagingData<Link>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = linkDataSource::selectPage
        ).flow.toLinks()
    }

    override fun selectRemoveAll(): Flow<PagingData<Link>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = linkDataSource::selectRemoveAll
        ).flow.toLinks()
    }

    override suspend fun incrementReadCount(id: Long) {
        linkDataSource.incrementReadCount(id)
    }

    override suspend fun setIsRemoveLink(id: Long, isRemove: Boolean) {
        linkDataSource.setIsRemoveLink(id, isRemove)
    }

    override fun selectLinksByTagName(tagName: String): Flow<PagingData<Link>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { linkDataSource.selectLinksByTagName(tagName) }
        ).flow.toLinks()
    }

    override suspend fun unRemoveLinks(linkIds: List<Long>) {
        linkDataSource.unRemoveLinks(linkIds)
    }

}