package com.linky.data.usecase.certification

import com.linky.data.repository.certification.CertificationRepository
import javax.inject.Inject

class CertifiedUseCase @Inject constructor(
    private val certificationRepository: CertificationRepository
) {

    operator fun invoke(password: String) = certificationRepository.certified(password)

}