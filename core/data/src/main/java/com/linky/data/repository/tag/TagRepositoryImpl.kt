package com.linky.data.repository.tag

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.linky.data_base.tag.data_source.TagDataSource
import com.linky.data_base.tag.entity.TagEntity
import com.linky.data_base.tag.entity.TagEntity.Companion.toEntity
import com.linky.data_base.tag.entity.TagEntity.Companion.toTag
import com.linky.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDataSource: TagDataSource
) : TagRepository {

    override suspend fun insert(tag: Tag): Long = tagDataSource.insert(tag.toEntity())

    override suspend fun delete(id: Long) = tagDataSource.delete(id)

    override fun selectAll(): Flow<PagingData<Tag>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = tagDataSource::selectAll
    ).flow.toTags()

    override suspend fun selectByIds(ids: List<Long>): List<Tag> =
        tagDataSource.selectByIds(ids).map { it.toTag() }

    override suspend fun updateUsedLink(tagIds: List<Long>, linkId: Long) =
        tagDataSource.updateUsedLink(tagIds, linkId)

}

private fun Flow<PagingData<TagEntity>>.toTags(): Flow<PagingData<Tag>> =
    map { it.map { it.toTag() } }