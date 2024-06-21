package com.linky.data.usecase.pin

import com.linky.data.repository.pin.PinRepository
import javax.inject.Inject

class SavePinUseCase @Inject constructor(
    private val pinRepository: PinRepository
) {
    suspend operator fun invoke(password: String) = pinRepository.setPin(password)
}