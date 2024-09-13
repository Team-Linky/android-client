package com.linky.di

import com.linky.data.repository.backup.DatabaseBackupRepository
import com.linky.data.repository.backup.DatabaseBackupRepositoryImpl
import com.linky.data.repository.link.LinkRepository
import com.linky.data.repository.link.LinkRepositoryImpl
import com.linky.data.repository.lock.LockRepository
import com.linky.data.repository.lock.LockRepositoryImpl
import com.linky.data.repository.pin.PinRepository
import com.linky.data.repository.pin.PinRepositoryImpl
import com.linky.data.repository.tag.TagRepository
import com.linky.data.repository.tag.TagRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsPinRepository(
        pinRepository: PinRepositoryImpl
    ): PinRepository

    @Binds
    @Singleton
    fun bindsLockRepository(
        lockRepository: LockRepositoryImpl
    ): LockRepository

    @Binds
    @Singleton
    fun bindsTagRepository(
        tagRepository: TagRepositoryImpl
    ): TagRepository

    @Binds
    @Singleton
    fun bindsLinkRepository(
        linkRepository: LinkRepositoryImpl
    ): LinkRepository

    @Binds
    @Singleton
    fun bindsDatabaseBackupRepository(
        databaseBackupRepository: DatabaseBackupRepositoryImpl
    ): DatabaseBackupRepository

}