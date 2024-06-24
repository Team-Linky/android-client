package com.linky.di

import com.linky.data.repository.link.LinkRepository
import com.linky.data.repository.link.LinkRepositoryImpl
import com.linky.data.repository.pin.PinRepository
import com.linky.data.repository.pin.PinRepositoryImpl
import com.linky.data.repository.lock.LockRepository
import com.linky.data.repository.lock.LockRepositoryImpl
import com.linky.data.repository.tag.TagRepository
import com.linky.data.repository.tag.TagRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsCertificationRepository(
        certificationRepository: PinRepositoryImpl
    ): PinRepository

    @Binds
    fun bindsLockRepository(
        lockRepository: LockRepositoryImpl
    ): LockRepository

    @Binds
    fun bindsTagRepository(
        tagRepository: TagRepositoryImpl
    ): TagRepository

    @Binds
    fun bindsLinkRepository(
        linkRepository: LinkRepositoryImpl
    ): LinkRepository

}