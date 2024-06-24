package com.linky.data.repository.link

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.linky.data_base.link.data_source.LinkDataSource
import com.linky.data_base.link.entity.LinkEntity.Companion.toEntity
import com.linky.data_base.link.entity.LinkEntity.Companion.toLink
import com.linky.data_base.link.entity.LinkWithTags
import com.linky.data_base.tag.entity.TagEntity.Companion.toEntity
import com.linky.model.Link
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LinkRepositoryImpl @Inject constructor(
    private val linkDataSource: LinkDataSource
) : LinkRepository {

    override suspend fun insert(link: Link): Long {
        return linkDataSource.insert(link.toEntity(), link.tags.map { it.toEntity() })
    }

    override suspend fun findLinkById(id: Long): Link? {
        return linkDataSource.findLinkById(id)
    }

    override suspend fun delete(id: Long) {
        linkDataSource.delete(id)
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

}

private fun Flow<PagingData<LinkWithTags>>.toLinks(): Flow<PagingData<Link>> {
    return map { it.map { it.link.toLink(it.tags) } }
}