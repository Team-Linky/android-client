package com.linky.di

import android.content.Context
import com.linky.data_base.LinkDataBase
import com.linky.data_base.link.dao.LinkDao
import com.linky.data_base.link.dao.LinkTagCrossRefDao
import com.linky.data_base.tag.dao.TagDao
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
    ): LinkDataBase = LinkDataBase.build(context, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    fun providesTagDao(
        linkDataBase: LinkDataBase
    ): TagDao = linkDataBase.getTagDao()

    @Provides
    fun providesLinkDao(
        linkDataBase: LinkDataBase
    ): LinkDao = linkDataBase.getLinkDao()

    @Provides
    fun providesLinkTagCrossRefDao(
        linkDataBase: LinkDataBase
    ): LinkTagCrossRefDao = linkDataBase.getLinkTagCrossRefDao()

}