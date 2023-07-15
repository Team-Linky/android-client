package com.linky.di

import com.linky.data.repository.certification.CertificationRepository
import com.linky.data.repository.certification.CertificationRepositoryImpl
import com.linky.data.repository.lock.LockRepository
import com.linky.data.repository.lock.LockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsCertificationRepository(
        certificationRepository: CertificationRepositoryImpl
    ): CertificationRepository

    @Binds
    fun bindsLockRepository(
        lockRepository: LockRepositoryImpl
    ): LockRepository

}