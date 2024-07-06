package com.linky.core.data_base.tag.entity

import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.linky.core.data_base.tag.entity.TagEntity.Companion.toTag
import com.linky.model.Tag
import com.linky.model.TagWithLinkCount
import com.linky.model.TagWithUsage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "tag")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk")
    val id: Long? = null,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "linkIds")
    val linkIds: List<Long>? = null
) {
    companion object {
        fun TagEntity.toTag(): Tag = Tag(
            id = id,
            name = name,
            linkIds = linkIds ?: emptyList()
        )

        fun Tag.toEntity(): TagEntity = TagEntity(
            id = id,
            name = name,
            linkIds = linkIds
        )

        fun Flow<PagingData<TagEntity>>.toTags(): Flow<PagingData<Tag>> =
            map { it.map { it.toTag() } }
    }
}

data class TagWithLinkCountEntity(
    @Embedded val tag: TagEntity,
    val linkCount: Int
) {
    companion object Mapper {
        fun TagWithLinkCountEntity.toTagWithLinkCount() = TagWithLinkCount(
            tag = tag.toTag(),
            linkCount = linkCount
        )

        fun Flow<PagingData<TagWithLinkCountEntity>>.toTagWithLinkCount() = map { it.map { it.toTagWithLinkCount() } }
    }
}

data class TagWithUsageEntity(
    @Embedded val tag: TagEntity,
    val isUsed: Boolean
) {
    companion object {
        fun TagWithUsageEntity.toTagWithUsage(): TagWithUsage = TagWithUsage(
            tag = tag.toTag(),
            isUsed = isUsed
        )

        fun Flow<PagingData<TagWithUsageEntity>>.toTagWithUsage(): Flow<PagingData<TagWithUsage>> =
            map { it.map { it.toTagWithUsage() } }
    }
}