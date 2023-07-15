package com.linky.data.usecase.lock

import com.linky.data.repository.lock.LockRepository
import javax.inject.Inject

class SetEnableLockUseCase @Inject constructor(
    private val lockRepository: LockRepository
) {
    suspend operator fun invoke(enable: Boolean) = lockRepository.enableLock(enable)
}