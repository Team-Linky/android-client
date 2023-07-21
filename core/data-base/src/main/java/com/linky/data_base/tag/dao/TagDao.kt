package com.linky.data_base.tag.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linky.data_base.tag.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tagEntity: TagEntity): Long

    @Query("DELETE FROM tag WHERE pk == :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM tag")
    fun selectAll(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag WHERE pk IN (:ids)")
    suspend fun selectByIds(ids: List<Long>): List<TagEntity>

    @Query("SELECT * FROM tag WHERE pk == :id")
    suspend fun findById(id: Long): TagEntity

}