package com.linky.di

import android.content.Context
import com.linky.core.data_base.LinkyDataBase
import com.linky.core.data_base.backup.LinkTagCrossRefBackupDao
import com.linky.core.data_base.link.dao.LinkDao
import com.linky.core.data_base.link.dao.LinkTagCrossRefDao
import com.linky.core.data_base.tag.dao.TagDao
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Provides
    @Singleton
    fun providesDataBase(
        @ApplicationContext context: Context,
        moshi: Moshi
    ): LinkyDataBase = LinkyDataBase.build(context, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun providesTagDao(
        linkyDataBase: LinkyDataBase
    ): TagDao = linkyDataBase.getTagDao()

    @Provides
    fun providesLinkDao(
        linkyDataBase: LinkyDataBase
    ): LinkDao = linkyDataBase.getLinkDao()

    @Provides
    fun providesLinkTagCrossRefDao(
        linkyDataBase: LinkyDataBase
    ): LinkTagCrossRefDao = linkyDataBase.getLinkTagCrossRefDao()

    @Provides
    fun providesLinkTagCrossRefBackupDao(
        linkyDataBase: LinkyDataBase
    ): LinkTagCrossRefBackupDao = linkyDataBase.getLinkTagCrossRefBackupDao()

}