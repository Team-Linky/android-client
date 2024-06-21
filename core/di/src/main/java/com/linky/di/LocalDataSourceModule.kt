package com.linky.di

import com.linky.data_base.link.data_source.LinkDataSource
import com.linky.data_base.link.data_source.LinkDataSourceImpl
import com.linky.data_base.tag.data_source.TagDataSource
import com.linky.data_base.tag.data_source.TagDataSourceImpl
import com.linky.data_store.data_source.certification.PinDataSource
import com.linky.data_store.data_source.certification.PinDataSourceImpl
import com.linky.data_store.data_source.lock.LockDataSource
import com.linky.data_store.data_source.lock.LockDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LocalDataSourceModule {

    @Binds
    fun bindsCertificationDataSource(
        certificationDataSource: PinDataSourceImpl
    ): PinDataSource

    @Binds
    fun bindsLockDataSource(
        lockDataSource: LockDataSourceImpl
    ): LockDataSource

    @Binds
    fun bindsTagDataSource(
        tagDataSourceImpl: TagDataSourceImpl
    ): TagDataSource

    @Binds
    fun bindsLinkDataSource(
        linkDataSourceImpl: LinkDataSourceImpl
    ): LinkDataSource

}