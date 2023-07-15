package com.linky.data.usecase.lock

import com.linky.data.repository.lock.LockRepository
import javax.inject.Inject

class IsEnableLockUseCase @Inject constructor(
    private val lockRepository: LockRepository
) {
    operator fun invoke() = lockRepository.isEnabled()
}