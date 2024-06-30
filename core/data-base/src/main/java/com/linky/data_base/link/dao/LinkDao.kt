package com.linky.data_base.link.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.link.entity.LinkWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(linkEntity: LinkEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinks(links: List<LinkEntity>): List<Long>

    @Query("DELETE FROM link WHERE pk == :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM link WHERE pk IN (:linkIds)")
    suspend fun deleteLinks(linkIds: List<Long>)

    @Query("DELETE FROM link WHERE isRemove == 1")
    suspend fun deleteRemovedAll()

    @Transaction
    @Query("SELECT * FROM link WHERE isRemove == 0")
    fun selectPage(): PagingSource<Int, LinkWithTags>

    @Query("SELECT * FROM link WHERE pk == :id")
    suspend fun findLinkById(id: Long): LinkEntity?

    @Transaction
    @Query("SELECT * FROM link WHERE pk = :linkId")
    suspend fun findLinkWithTagsById(linkId: Long): LinkWithTags

    @Query("UPDATE link SET readCount = readCount + 1 WHERE pk == :id")
    suspend fun incrementReadCount(id: Long)

    @Transaction
    @Query(
    """
        SELECT * FROM link WHERE pk IN (
            SELECT linkId FROM linktagcrossref WHERE tagId IN (
                SELECT pk FROM tag WHERE name LIKE '%' || :tagName || '%'
            )
        )
        AND isRemove == 0
    """
    )
    fun selectLinksByTagName(tagName: String): PagingSource<Int, LinkWithTags>

    @Query("SELECT * FROM link WHERE isRemove == 1")
    fun selectRemoveAll(): PagingSource<Int, LinkEntity>

    @Query("UPDATE link SET isRemove = :isRemove WHERE pk == :id")
    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

    @Query("SELECT COUNT(*) as linkCount FROM link WHERE isRemove == 1")
    fun getRemoveLinkCount(): Flow<Int>

    @Query("UPDATE link SET isRemove = 0 WHERE pk IN (:linkIds)")
    suspend fun unRemoveLinks(linkIds: List<Long>)

}