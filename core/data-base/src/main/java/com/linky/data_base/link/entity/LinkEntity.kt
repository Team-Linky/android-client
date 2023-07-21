package com.linky.data_base.link.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.linky.model.Link
import com.linky.model.open_graph.OpenGraphData

@Entity(tableName = "link")
data class LinkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk")
    val id: Long? = null,

    @ColumnInfo(name = "memo")
    val memo: String,

    @ColumnInfo(name = "openGraphData")
    val openGraphData: OpenGraphData,

    @ColumnInfo(name = "tags")
    val tags: List<Long> = emptyList(),

    @ColumnInfo(name = "readCount")
    val readCount: Long = 0,

    @ColumnInfo(name = "createAt")
    val createAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "isRemove")
    val isRemove: Boolean = false
) {
    companion object {
        fun Link.toEntity(): LinkEntity = LinkEntity(
            id = id,
            memo = memo,
            openGraphData = openGraphData,
            tags = tags,
            readCount = readCount,
            createAt = createAt,
            isRemove = isRemove
        )

        fun LinkEntity.toLink(): Link = Link(
            id = id,
            memo = memo,
            openGraphData = openGraphData,
            tags = tags,
            readCount = readCount,
            createAt = createAt,
            isRemove = isRemove
        )
    }
}