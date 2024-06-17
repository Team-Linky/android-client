package com.linky.data.usecase.lock

import com.linky.data.repository.lock.LockRepository
import javax.inject.Inject

class GetEnableLockUseCase @Inject constructor(
    lockRepository: LockRepository
) {
    val state = lockRepository.enableLock
}