package com.linky.core.data_base.link.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linky.core.data_base.link.entity.LinkTagCrossRef

@Dao
interface LinkTagCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkTagCrossRef(crossRef: LinkTagCrossRef)

    @Query("DELETE FROM linktagcrossref WHERE linkId = :linkId")
    suspend fun deleteReferencesByLinkId(linkId: Long)

    @Query("DELETE FROM LinkTagCrossRef WHERE linkId = :linkId AND tagId IN (:tagIds)")
    suspend fun deleteLinkTagCrossRefs(linkId: Long, tagIds: List<Long>)

    @Query("SELECT * FROM LinkTagCrossRef WHERE linkId = :linkId")
    suspend fun getReferencesByLinkId(linkId: Long): List<LinkTagCrossRef>

}