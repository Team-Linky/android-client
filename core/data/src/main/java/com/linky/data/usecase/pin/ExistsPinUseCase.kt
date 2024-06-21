package com.linky.data.usecase.pin

import com.linky.data.repository.pin.PinRepository
import javax.inject.Inject

class ExistsPasswordUseCase @Inject constructor(
    private val pinRepository: PinRepository
) {
    operator fun invoke() = pinRepository.existPin()
}