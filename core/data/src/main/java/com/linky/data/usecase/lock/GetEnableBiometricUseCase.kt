package com.linky.data.usecase.lock

import com.linky.data.repository.lock.LockRepository
import javax.inject.Inject

class GetEnableBiometricUseCase @Inject constructor(
    lockRepository: LockRepository
) {
    val state = lockRepository.enableBiometric
}