package com.linky.data_base.tag.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linky.data_base.tag.entity.TagEntity

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tagEntity: TagEntity): Long

    @Query("DELETE FROM tag WHERE pk == :id")
    suspend fun delete(id: Long)

    @Query("""
        SELECT tag.*, COUNT(linktagcrossref.linkId) as linkCount
        FROM tag
        LEFT JOIN linktagcrossref ON tag.pk = linktagcrossref.tagId
        GROUP BY tag.pk
        ORDER BY linkCount DESC
    """)
    fun selectAll(): PagingSource<Int, TagEntity>

    @Query("SELECT * FROM tag WHERE pk IN (:ids)")
    suspend fun selectByIds(ids: List<Long>): List<TagEntity>

    @Query("SELECT * FROM tag WHERE pk == :id")
    suspend fun findById(id: Long): TagEntity

}