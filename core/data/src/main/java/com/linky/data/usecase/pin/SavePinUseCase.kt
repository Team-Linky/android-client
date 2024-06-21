package com.linky.data.usecase.certification

import com.linky.data.repository.certification.CertificationRepository
import javax.inject.Inject

class SavePinUseCase @Inject constructor(
    private val certificationRepository: CertificationRepository
) {
    suspend operator fun invoke(password: String) = certificationRepository.setPassword(password)
}