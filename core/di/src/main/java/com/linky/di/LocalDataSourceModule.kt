package com.linky.di

import com.linky.data_store.data_source.certification.CertificationDataSource
import com.linky.data_store.data_source.certification.CertificationDataSourceImpl
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
        certificationDataSource: CertificationDataSourceImpl
    ): CertificationDataSource

    @Binds
    fun bindsLockDataSource(
        lockDataSource: LockDataSourceImpl
    ): LockDataSource

}