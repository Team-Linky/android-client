package com.linky.core.data_base.tag.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.linky.core.data_base.tag.entity.TagEntity
import com.linky.core.data_base.tag.entity.TagWithLinkCountEntity
import com.linky.core.data_base.tag.entity.TagWithUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tagEntity: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>): List<Long>

    @Query("DELETE FROM tag WHERE pk == :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM tag WHERE pk IN (:tagIds)")
    suspend fun deleteTagsByIds(tagIds: List<Long>)

    @Query("SELECT * FROM tag WHERE pk IN (:ids)")
    suspend fun selectByIds(ids: List<Long>): List<TagEntity>

    @Query("SELECT COUNT(*) as tagCount FROM tag")
    fun getTagCount(): Flow<Int>

    @Transaction
    @Query(
        """
        SELECT tag.*, COUNT(linktagcrossref.linkId) as linkCount
        FROM tag
        LEFT JOIN linktagcrossref ON tag.pk = linktagcrossref.tagId
        GROUP BY tag.pk
        ORDER BY linkCount DESC
        """
    )
    fun selectAll(): PagingSource<Int, TagEntity>

    @Transaction
    @Query(
        """
        SELECT tag.*, COUNT(linktagcrossref.linkId) as linkCount FROM tag
        LEFT JOIN linktagcrossref ON tag.pk = linktagcrossref.tagId 
        GROUP BY tag.pk
        HAVING COUNT(linktagcrossref.linkId) >= :min
        ORDER BY linkCount DESC
        """
    )
    fun selectAllWithLinkCount(min: Int): PagingSource<Int, TagEntity>

    @Query(
        """
        SELECT tag.*, 
               EXISTS (SELECT 1 
                       FROM linktagcrossref 
                       WHERE linktagcrossref.tagId = tag.pk AND linktagcrossref.linkId = :linkId) 
               AS isUsed
        FROM tag
        """
    )
    fun selectAllWithUsage(linkId: Long): PagingSource<Int, TagWithUsageEntity>

    @Transaction
    @Query(
        """
            SELECT tag.*, COUNT(linktagcrossref.linkId) as linkCount 
            FROM tag
            LEFT JOIN linktagcrossref ON tag.pk = linktagcrossref.tagId 
            GROUP BY tag.pk
            ORDER BY tag.pk
        """
    )
    fun selectTagsWithLinkCount(): PagingSource<Int, TagWithLinkCountEntity>

}