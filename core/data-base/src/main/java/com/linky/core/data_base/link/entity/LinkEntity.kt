package com.linky.core.data_base.link.entity

import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.linky.core.data_base.link.entity.LinkEntity.Mapper.toLink
import com.linky.core.data_base.tag.entity.TagEntity
import com.linky.core.data_base.tag.entity.TagEntity.Companion.toTag
import com.linky.model.Link
import com.linky.model.open_graph.OpenGraphData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "link")
data class LinkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk")
    val id: Long? = null,

    @ColumnInfo(name = "memo")
    val memo: String,

    @ColumnInfo(name = "openGraphData")
    val openGraphData: OpenGraphData,

    @ColumnInfo(name = "readCount")
    val readCount: Long = 0,

    @ColumnInfo(name = "createAt")
    val createAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "isRemove")
    val isRemove: Boolean = false
) {
    companion object Mapper {
        fun Link.toEntity(): LinkEntity = LinkEntity(
            id = id,
            memo = memo,
            openGraphData = openGraphData,
            readCount = readCount,
            createAt = createAt,
            isRemove = isRemove
        )

        fun LinkEntity.toLink(tags: List<TagEntity> = listOf()): Link = Link(
            id = id,
            memo = memo,
            tags = tags.map { it.toTag() },
            openGraphData = openGraphData,
            readCount = readCount,
            createAt = createAt,
            isRemove = isRemove
        )

        fun Flow<PagingData<LinkEntity>>.toLinks(): Flow<PagingData<Link>> {
            return map { it.map { it.toLink() } }
        }

    }
}

@Entity(
    primaryKeys = ["linkId", "tagId"],
    indices = [Index(value = ["tagId"])]
)
data class LinkTagCrossRef(
    val linkId: Long,
    val tagId: Long
)

data class LinkWithTags(
    @Embedded val link: LinkEntity,
    @Relation(
        parentColumn = "pk",
        entityColumn = "pk",
        associateBy = Junction(
            value = LinkTagCrossRef::class,
            parentColumn = "linkId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagEntity>
) {
    companion object Mapper {

        fun Flow<PagingData<LinkWithTags>>.toLinks(): Flow<PagingData<Link>> {
            return map { it.map { it.link.toLink(it.tags) } }
        }

    }
}