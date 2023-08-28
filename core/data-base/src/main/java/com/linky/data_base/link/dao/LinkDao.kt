package com.linky.data_base.link.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linky.data_base.link.entity.LinkEntity

@Dao
interface LinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(linkEntity: LinkEntity): Long

    @Query("DELETE FROM link WHERE pk == :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM link WHERE isRemove == 0")
    fun selectPage(): PagingSource<Int, LinkEntity>

    @Query("UPDATE link SET readCount = readCount + 1 WHERE pk == :id")
    suspend fun incrementReadCount(id: Long)

    @Query("UPDATE link SET isRemove = :isRemove WHERE pk == :id")
    suspend fun setIsRemoveLink(id: Long, isRemove: Boolean)

}