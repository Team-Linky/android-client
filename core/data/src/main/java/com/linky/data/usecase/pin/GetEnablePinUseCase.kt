package com.linky.data.usecase.certification

import com.linky.data.repository.certification.CertificationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetEnablePinUseCase @Inject constructor(
    private val certificationRepository: CertificationRepository
) {

    suspend operator fun invoke(pin: String) = certificationRepository.certified(pin).first()

}