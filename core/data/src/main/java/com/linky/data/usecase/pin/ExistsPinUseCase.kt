package com.linky.data.usecase.pin

import com.linky.data.repository.pin.PinRepository
import javax.inject.Inject

class ExistsPinUseCase @Inject constructor(
    private val pinRepository: PinRepository
) {
    val state = pinRepository.pin
}