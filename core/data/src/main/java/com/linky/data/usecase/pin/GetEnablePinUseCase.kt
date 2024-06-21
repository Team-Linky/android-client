package com.linky.data.usecase.pin

import com.linky.data.repository.pin.PinRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetEnablePinUseCase @Inject constructor(
    private val pinRepository: PinRepository
) {

    suspend operator fun invoke(pin: String) = pinRepository.certified(pin).first()

}