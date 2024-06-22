package com.linky.data_base.link.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.link.entity.LinkTagCrossRef
import com.linky.data_base.link.entity.LinkWithTags
import com.linky.data_base.tag.entity.TagEntity

@Dao
interface LinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(linkEntity: LinkEntity): Long

    @Query("DELETE FROM link WHERE pk == :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM link WHERE isRemove == 0")
    fun selectPage(): PagingSource<Int, LinkWithTags>

    @Query("UPDATE link SET readCount = readCount + 1 WHERE pk == :id")
    suspend fun incrementReadCount(id: Long)

    @Query("UPDATE link SET isRemove = :isRemove WHERE pk == :id")
    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRef(crossRef: LinkTagCrossRef)

    @Transaction
    suspend fun insertLinkWithTags(
        linkEntity: LinkEntity,
        tagEntities: List<TagEntity>
    ): Long {
        val linkId = insert(linkEntity)

        tagEntities.forEach { insertLinkTagCrossRef(LinkTagCrossRef(linkId, it.id ?: 0L)) }

        return linkId
    }

    @Transaction
    @Query("""
        SELECT *
        FROM link
        WHERE pk IN (
            SELECT linkId
            FROM linktagcrossref
            WHERE tagId IN (
                SELECT pk
                FROM tag
                WHERE name LIKE '%' || :tagName || '%'
            )
        )
        AND isRemove == 0
    """)
    fun selectLinksByTagName(tagName: String): PagingSource<Int, LinkWithTags>

}