package com.linky.data_base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.linky.data_base.converter.LongTypeListConverter
import com.linky.data_base.converter.OpenGraphDataConverter
import com.linky.data_base.link.dao.LinkDao
import com.linky.data_base.link.entity.LinkEntity
import com.linky.data_base.tag.dao.TagDao
import com.linky.data_base.tag.entity.TagEntity
import com.squareup.moshi.Moshi

@Database(entities = [TagEntity::class, LinkEntity::class], version = 3)
@TypeConverters(value = [LongTypeListConverter::class, OpenGraphDataConverter::class])
abstract class LinkDataBase : RoomDatabase() {

    abstract fun getTagDao(): TagDao
    abstract fun getLinkDao(): LinkDao

    companion object {
        private const val DATABASE_NAME = "linky_db"

        fun build(context: Context, moshi: Moshi) = Room.databaseBuilder(
            context = context,
            klass = LinkDataBase::class.java,
            name = DATABASE_NAME
        )
            .addTypeConverter(LongTypeListConverter(moshi))
            .addTypeConverter(OpenGraphDataConverter(moshi))
            .fallbackToDestructiveMigration().build()
    }

}