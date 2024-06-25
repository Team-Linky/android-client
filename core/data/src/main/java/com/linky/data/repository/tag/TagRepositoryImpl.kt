package com.linky.data.repository.tag

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.linky.data_base.tag.data_source.TagDataSource
import com.linky.data_base.tag.entity.TagEntity.Companion.toEntity
import com.linky.data_base.tag.entity.TagEntity.Companion.toTag
import com.linky.data_base.tag.entity.TagEntity.Companion.toTags
import com.linky.data_base.tag.entity.TagWithLinkCountEntity.Mapper.toTagWithLinkCount
import com.linky.data_base.tag.entity.TagWithUsageEntity.Companion.toTagWithUsage
import com.linky.model.Tag
import com.linky.model.TagWithLinkCount
import com.linky.model.TagWithUsage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDataSource: TagDataSource
) : TagRepository {

    private val _deletedTagsCache = mutableListOf<Tag>()
    override val deletedTagsCache: List<Tag> get() = _deletedTagsCache

    override suspend fun insert(tag: Tag): Long {
        return tagDataSource.insert(tag.toEntity())
    }

    override suspend fun insertTags(tags: List<Tag>): List<Long> {
        return tagDataSource.insertTags(tags.map { it.toEntity() })
    }

    override suspend fun delete(id: Long) {
        tagDataSource.delete(id)
    }

    override suspend fun deleteTagsByIds(tags: List<Tag>) {
        _deletedTagsCache.clear()
        _deletedTagsCache += tags

        tagDataSource.deleteTagsByIds(tags.mapNotNull { it.id })
    }

    override fun getTagCount(): Flow<Int> {
        return tagDataSource.getTagCount()
    }

    override fun selectAll(): Flow<PagingData<Tag>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = tagDataSource::selectAll
        ).flow.toTags()
    }

    override fun selectAllWithLinkCount(count: Int): Flow<PagingData<Tag>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { tagDataSource.selectAllWithLinkCount(count) }
        ).flow.toTags()
    }

    override suspend fun selectByIds(ids: List<Long>): List<Tag> {
        return tagDataSource.selectByIds(ids).map { it.toTag() }
    }

    override suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long) {
        return tagDataSource.updateUsedLink(tagIds, linkId)
    }

    override fun selectAllWithUsage(linkId: Long, pageSize: Int): Flow<PagingData<TagWithUsage>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { tagDataSource.selectAllWithUsage(linkId) }
        ).flow.toTagWithUsage()
    }

    override fun selectTagsWithLinkCount(): Flow<PagingData<TagWithLinkCount>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = tagDataSource::selectTagsWithLinkCount
        ).flow.toTagWithLinkCount()
    }

}