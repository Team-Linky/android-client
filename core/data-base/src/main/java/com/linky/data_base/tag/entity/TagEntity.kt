package com.linky.data_base.tag.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.linky.model.Tag
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

        fun Flow<List<TagEntity>>.toTag(): Flow<List<Tag>> = map { list -> list.map { it.toTag() } }
    }
}
