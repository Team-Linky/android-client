package com.linky.lock.state

data class LockState(
    val lockStatus: LockStatus,
    val biometricStatus: BiometricStatus
) {
    companion object {
        val Init: LockState
            get() = LockState(
                lockStatus = LockStatus.Loading,
                biometricStatus = BiometricStatus.Loading,
            )
    }
}

sealed interface LockStatus {
    data object Loading : LockStatus
    data class Result(val enable: Boolean) : LockStatus
}

sealed interface BiometricStatus {
    data object Loading : BiometricStatus
    data class Result(val enable: Boolean) : BiometricStatus
}
