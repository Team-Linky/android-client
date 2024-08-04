package com.linky.core.data_base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.linky.core.data_base.backup.LinkTagCrossRefBackupDao
import com.linky.core.data_base.backup.entity.LinkTagCrossRefBackupEntity
import com.linky.core.data_base.converter.LongTypeListConverter
import com.linky.core.data_base.converter.OpenGraphDataConverter
import com.linky.core.data_base.link.dao.LinkDao
import com.linky.core.data_base.link.dao.LinkTagCrossRefDao
import com.linky.core.data_base.link.entity.LinkEntity
import com.linky.core.data_base.link.entity.LinkTagCrossRef
import com.linky.core.data_base.tag.dao.TagDao
import com.linky.core.data_base.tag.entity.TagEntity
import com.squareup.moshi.Moshi

@Database(
    entities = [
        TagEntity::class,
        LinkEntity::class,
        LinkTagCrossRef::class,
        LinkTagCrossRefBackupEntity::class
    ],
    version = 2
)
@TypeConverters(value = [LongTypeListConverter::class, OpenGraphDataConverter::class])
abstract class LinkyDataBase : RoomDatabase() {

    abstract fun getTagDao(): TagDao
    abstract fun getLinkDao(): LinkDao
    abstract fun getLinkTagCrossRefDao(): LinkTagCrossRefDao
    abstract fun getLinkTagCrossRefBackupDao(): LinkTagCrossRefBackupDao

    companion object {
        private const val DATABASE_NAME = "linky_db"

        fun build(context: Context, moshi: Moshi) = Room.databaseBuilder(
            context = context,
            klass = LinkyDataBase::class.java,
            name = DATABASE_NAME
        )
            .addTypeConverter(LongTypeListConverter(moshi))
            .addTypeConverter(OpenGraphDataConverter(moshi))
            .fallbackToDestructiveMigration()
            .build()
    }

}