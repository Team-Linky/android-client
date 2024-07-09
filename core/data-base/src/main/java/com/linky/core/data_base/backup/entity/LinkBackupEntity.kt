package com.linky.core.data_base.backup.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "linktagcrossref_backup")
data class LinkTagCrossRefBackupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "linkId")
    val linkId: Long,

    @ColumnInfo(name = "tagId")
    val tagId: Long
)