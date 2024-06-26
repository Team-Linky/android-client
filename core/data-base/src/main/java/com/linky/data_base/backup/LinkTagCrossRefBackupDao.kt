package com.linky.data_base.backup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linky.data_base.backup.entity.LinkTagCrossRefBackupEntity

@Dao
interface LinkTagCrossRefBackupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackup(crossRefBackup: LinkTagCrossRefBackupEntity)

    @Query("DELETE FROM linktagcrossref_backup WHERE linkId = :linkId")
    suspend fun deleteBackupByLinkId(linkId: Long)

    @Query("SELECT * FROM linktagcrossref_backup WHERE linkId = :linkId")
    suspend fun getBackupByLinkId(linkId: Long): List<LinkTagCrossRefBackupEntity>
}