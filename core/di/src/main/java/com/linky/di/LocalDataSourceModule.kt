package com.linky.di

import com.linky.core.data_base.link.data_source.LinkDataSource
import com.linky.core.data_base.link.data_source.LinkDataSourceImpl
import com.linky.core.data_base.tag.data_source.TagDataSource
import com.linky.core.data_base.tag.data_source.TagDataSourceImpl
import com.linky.data_store.data_source.pin.PinDataSource
import com.linky.data_store.data_source.pin.PinDataSourceImpl
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
    fun bindsPinDataSource(
        pinDataSource: PinDataSourceImpl
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