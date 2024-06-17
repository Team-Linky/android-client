package com.linky.data.repository.lock

import com.linky.data_store.data_source.lock.LockDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LockRepositoryImpl @Inject constructor(
    private val lockDataSource: LockDataSource
) : LockRepository{

    override suspend fun enableLock(enable: Boolean) = lockDataSource.enableLock(enable)
    override val enableLock: Flow<Boolean> = lockDataSource.enableLock

    override suspend fun enableBiometric(enable: Boolean) = lockDataSource.enableBiometric(enable)
    override val enableBiometric: Flow<Boolean> = lockDataSource.enableBiometric

}